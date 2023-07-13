package com.kit.feedback.dto;

import com.kit.feedback.model.Lecturer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LecturersResponse {
    private List<Lecturer> contents;
    private long count;
}
