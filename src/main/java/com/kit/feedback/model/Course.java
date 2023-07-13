package com.kit.feedback.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Course extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private Integer credit;
    @ManyToOne
    @JsonBackReference(value = "lecturer-courses")
    private Lecturer lecturer;

    @ManyToOne
    @JsonBackReference(value = "student-courses")
    private Student student;

    @OneToMany(mappedBy = "course")
    @JsonManagedReference(value = "course-feedbackForms")
    private List<FeedbackForm> feedbackForms;

    @ManyToOne
    @JoinColumn(name = "semester_id")
    @JsonBackReference(value = "semester-courses")
    private Semester semester;
}
