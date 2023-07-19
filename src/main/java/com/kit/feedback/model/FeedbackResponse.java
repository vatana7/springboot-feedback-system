package com.kit.feedback.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FeedbackResponse {
    private String question;
    private List<ResponseQuestion> voteResult;
}
