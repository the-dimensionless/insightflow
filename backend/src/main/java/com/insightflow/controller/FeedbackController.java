package com.insightflow.controller;

import com.insightflow.model.Feedback;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final KafkaTemplate<String, Feedback> kafkaTemplate;

    @PostMapping
    public ResponseEntity<String> postFeedback(@RequestBody Feedback feedback) {
        kafkaTemplate.send("feedback-input", feedback);
        return ResponseEntity.ok("Feedback submitted.");
    }
} 