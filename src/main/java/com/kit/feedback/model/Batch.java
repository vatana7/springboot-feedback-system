package com.kit.feedback.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Batch extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "department_id")
    @JsonBackReference(value = "department-batches")
    private Department department;
    private Integer batchNumber;
    @OneToMany(mappedBy = "batch", cascade = CascadeType.REMOVE)
    @JsonManagedReference(value = "batch-semesters")
    private List<Semester> semesters;
}








