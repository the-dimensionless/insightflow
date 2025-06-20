package com.insightflow.service;

import com.insightflow.model.Feedback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class FeedbackService {
    
    @Autowired
    private OpenAIService openAIService;
    
    @Autowired
    private RedisTemplate<String, Feedback> redisTemplate;
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    private static final String REDIS_KEY_PREFIX = "feedback:";
    private static final String REDIS_LIST_KEY = "feedback:all";
    
    public Feedback processFeedback(Feedback feedback) {
        // Enrich with AI
        Feedback enrichedFeedback = openAIService.enrichFeedback(feedback);
        
        // Store in Redis
        saveToRedis(enrichedFeedback);
        
        // Send to WebSocket
        sendToWebSocket(enrichedFeedback);
        
        return enrichedFeedback;
    }
    
    private void saveToRedis(Feedback feedback) {
        // Store individual feedback
        String key = REDIS_KEY_PREFIX + feedback.getId();
        redisTemplate.opsForValue().set(key, feedback, 24, TimeUnit.HOURS);
        
        // Add to list of all feedback
        redisTemplate.opsForList().leftPush(REDIS_LIST_KEY, feedback);
        
        // Keep only last 100 feedback items
        redisTemplate.opsForList().trim(REDIS_LIST_KEY, 0, 99);
    }
    
    private void sendToWebSocket(Feedback feedback) {
        messagingTemplate.convertAndSend("/topic/feedback", feedback);
    }
    
    public List<Feedback> getAllFeedback() {
        return redisTemplate.opsForList().range(REDIS_LIST_KEY, 0, -1);
    }
    
    public Feedback getFeedbackById(String id) {
        return redisTemplate.opsForValue().get(REDIS_KEY_PREFIX + id);
    }
    
    public List<Feedback> getFeedbackBySentiment(String sentiment) {
        List<Feedback> allFeedback = getAllFeedback();
        return allFeedback.stream()
                .filter(f -> sentiment.equals(f.getSentiment()))
                .toList();
    }
    
    public List<Feedback> getFeedbackByCategory(String category) {
        List<Feedback> allFeedback = getAllFeedback();
        return allFeedback.stream()
                .filter(f -> category.equals(f.getCategory()))
                .toList();
    }
} 