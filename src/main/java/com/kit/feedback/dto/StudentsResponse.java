package com.kit.feedback.dto;

import com.kit.feedback.model.Student;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentsResponse {
    private List<Student> content;
    private long count;
}
