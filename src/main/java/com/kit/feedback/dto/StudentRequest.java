package com.kit.feedback.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class StudentRequest {
    private UUID courseId;
    private UUID studentId;
}
