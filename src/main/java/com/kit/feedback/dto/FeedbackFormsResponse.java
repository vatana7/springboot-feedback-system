package com.kit.feedback.dto;

import com.kit.feedback.model.FeedbackForm;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FeedbackFormsResponse {
    private List<FeedbackForm> content;
    private long count;
}
