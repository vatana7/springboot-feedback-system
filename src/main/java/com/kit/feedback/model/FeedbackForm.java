package com.kit.feedback.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedbackForm extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String title;
    private String description;

    @ManyToOne
    @JoinColumn(name = "feedback-course-id")
    @JsonBackReference(value = "course-feedbackForms")
    private Course course;
    @ElementCollection
    private List<String> questions;
}
