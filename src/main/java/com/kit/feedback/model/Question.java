package com.kit.feedback.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String questionText;
    private Integer rating;
    private Integer questionNumber;

    @ManyToOne
    @JsonBackReference(value = "feedbackForm-questions")
    private FeedbackForm feedbackForm;

}
