package com.kit.feedback.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "feedbackForm")
    private List<Question> listOfQuestions;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "feedbackForm")
    private List<Feedback> listOfFeedbacks;
}
