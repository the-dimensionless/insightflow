package com.insightflow.model;

import lombok.Data;

@Data
public class FeedbackEnriched {
    private String userId;
    private String message;
    private long timestamp;
    private String sentiment;
    private String category;
    private String summary;
} 