package com.kit.feedback.repository;

import com.kit.feedback.model.FeedbackForm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FeedbackFormRepository extends JpaRepository<FeedbackForm, UUID> {

    void deleteById(UUID id);

    Optional<FeedbackForm> findById(UUID id);

}
