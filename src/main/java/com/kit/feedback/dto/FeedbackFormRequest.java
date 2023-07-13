package com.kit.feedback.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class FeedbackFormRequest {
    private UUID courseId;
    private String title;
    private String description;
    private List<String> questions;
}
