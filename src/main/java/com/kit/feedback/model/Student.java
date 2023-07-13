package com.kit.feedback.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    private String name;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToMany
    @JoinColumn(name = "student-course_id")
    @JsonManagedReference(value ="student-courses")
    private List<Course> courses;

    @OneToOne
    @JoinColumn(name = "student")
    private User user;

//    Todo: field for courses and semester
}
