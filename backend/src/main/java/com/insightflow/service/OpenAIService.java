package com.insightflow.service;

import com.insightflow.model.Feedback;
import com.insightflow.model.FeedbackEnriched;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OpenAIService {

    private final RestTemplate restTemplate;

    @Value("${openai.api-key}")
    private String apiKey;

    public FeedbackEnriched enrichFeedback(Feedback feedback) {
        String prompt = String.format("""
            Analyze the following feedback:
            "%s"
            Return JSON like:
            {
              \"sentiment\": \"...\",
              \"category\": \"...\",
              \"summary\": \"...\"
            }
            """, feedback.getMessage());

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
            "model", "gpt-3.5-turbo",
            "messages", List.of(
                Map.of("role", "user", "content", prompt)
            )
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                "https://api.openai.com/v1/chat/completions", request, String.class
            );

            JSONObject obj = new JSONObject(response.getBody());
            String content = obj.getJSONArray("choices")
                                .getJSONObject(0)
                                .getJSONObject("message")
                                .getString("content");

            JSONObject enriched = new JSONObject(content);
            FeedbackEnriched result = new FeedbackEnriched();
            result.setUserId(feedback.getUserId());
            result.setMessage(feedback.getMessage());
            result.setTimestamp(feedback.getTimestamp());
            result.setSentiment(enriched.optString("sentiment"));
            result.setCategory(enriched.optString("category"));
            result.setSummary(enriched.optString("summary"));
            return result;

        } catch (Exception e) {
            throw new RuntimeException("OpenAI call failed", e);
        }
    }
} 