package com.kit.feedback.services;

import com.kit.feedback.dto.FeedbackFormRequest;
import com.kit.feedback.dto.FeedbackFormsResponse;
import com.kit.feedback.model.FeedbackForm;
import com.kit.feedback.model.User;
import com.kit.feedback.repository.CourseRepository;
import com.kit.feedback.repository.FeedbackFormRepository;
import com.kit.feedback.utils.Utility;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedbackFormService {
    private final FeedbackFormRepository feedbackFormRepository;
    private final CourseRepository courseRepository;

    public FeedbackForm create(FeedbackFormRequest request){
        var user = (User)Utility.getCurrentAuthentication().getPrincipal();
        var course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Cannot find course, ID: " + request.getCourseId()));
        var feedbackForm = FeedbackForm.builder().build();
        feedbackForm.setTitle(request.getTitle());
        feedbackForm.setDescription(request.getDescription());
        feedbackForm.setQuestions(request.getQuestions());
        feedbackForm.setCreatedBy(user.getId());
        feedbackForm.setCreatedAt(LocalDateTime.now().toString());
        feedbackForm.setCourse(course);
        feedbackForm.setUpdatedAt(LocalDateTime.now().toString());
        feedbackForm.setUpdatedBy(user.getId());
        try{
            var saved = feedbackFormRepository.save(feedbackForm);
            return saved;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public FeedbackFormsResponse getAll(Integer page, Integer size){
        PageRequest pageRequest = PageRequest.of(page, size);
        var feedbackForms = feedbackFormRepository.findAll(pageRequest);
        return FeedbackFormsResponse.builder()
                .content(feedbackForms.getContent())
                .count(feedbackFormRepository.count())
                .build();
    }

    public String delete(UUID id){
        try{
            feedbackFormRepository.deleteById(id);
            return "Delete FeedbackForm successfully, ID: " + id;
        }catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
