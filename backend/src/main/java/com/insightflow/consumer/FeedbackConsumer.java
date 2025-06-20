package com.insightflow.consumer;

import com.insightflow.model.Feedback;
import com.insightflow.model.FeedbackEnriched;
import com.insightflow.service.OpenAIService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackConsumer {

    private final OpenAIService openAIService;
    private final RedisTemplate<String, FeedbackEnriched> redisTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = "feedback-input", groupId = "feedback-group")
    public void consume(Feedback feedback) {
        FeedbackEnriched enriched = openAIService.enrichFeedback(feedback);

        // Store in Redis
        redisTemplate.opsForValue().set("feedback:" + feedback.getUserId(), enriched);

        // Push to frontend via WebSocket
        messagingTemplate.convertAndSend("/topic/feedback", enriched);
    }
} 