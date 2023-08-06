package com.kit.feedback.dto;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.kit.feedback.model.Batch;
import com.kit.feedback.model.Course;
import com.kit.feedback.model.Semester;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SemesterResponse {

    private UUID id;
    private Integer semesterNumber;
    private Integer credit;
    private LocalDate startDate;
    private LocalDate endDate;

    private List<Course> courses;

    private Integer batchNumber;
    private String department;


    //!Return batch number and department in each semester in get-all semesters
}
