package com.kit.feedback.repository;

import com.kit.feedback.dto.FeedbackResult;
import com.kit.feedback.model.FeedbackResponse;
import com.kit.feedback.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    @Query(value = "SELECT id, question_number, rating, feedback_id,feedback_form_id,question_text, COUNT(*) AS count FROM question WHERE feedback_form_id = :id GROUP BY id, question_number, rating, feedback_id,feedback_form_id,question_text ORDER BY id, question_number, rating, feedback_id,feedback_form_id,question_text ASC", nativeQuery = true)
    List<FeedbackResult> findResponseById(@Param("id") Integer id);

}
