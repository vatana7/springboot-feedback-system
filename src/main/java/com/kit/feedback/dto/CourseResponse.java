package com.kit.feedback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@Builder
public class CourseResponse {
    private String name;
    private UUID id;
    private String lecturerName;
    private Integer totalVote;
}
