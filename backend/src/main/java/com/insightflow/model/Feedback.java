package com.insightflow.model;

import lombok.Data;

@Data
public class Feedback {
    private String userId;
    private String message;
    private long timestamp;
} 