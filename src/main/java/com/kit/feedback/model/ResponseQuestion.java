package com.kit.feedback.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseQuestion {
    private Integer rating;
    private Integer voteNumber;
}
