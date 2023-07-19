package com.kit.feedback.dto;

import com.kit.feedback.model.Question;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class FeedbackRequest {
    private Integer feedbackFormId;
    private List<Question> questions;
}
