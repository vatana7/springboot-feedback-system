package com.kit.feedback.dto;

import com.kit.feedback.model.BaseEntity;
import com.kit.feedback.model.Semester;
import lombok.Data;

import java.util.UUID;

@Data
public class CourseRequest extends BaseEntity {
    private String name;
    private Integer credit;
    private UUID semesterId;
    private UUID lecturerId;
    private UUID courseId;
}
