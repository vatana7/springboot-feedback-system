package com.kit.feedback.services;

import com.kit.feedback.dto.FeedbackRequest;
import com.kit.feedback.enums.Role;
import com.kit.feedback.model.Feedback;
import com.kit.feedback.model.User;
import com.kit.feedback.repository.FeedbackFormRepository;
import com.kit.feedback.repository.FeedbackRepository;
import com.kit.feedback.utils.Utility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final FeedbackFormRepository feedbackFormRepository;

    public Feedback saveFeedback(FeedbackRequest request){
        try{
            var user = (User) Utility.getCurrentAuthentication().getPrincipal();
            var courses = user.getStudent().getCourses().stream().map(item -> item.getName()).collect(Collectors.toList());
            if(!user.getRole().equals(Role.STUDENT)){
                throw new RuntimeException("Cannot submit because role is not student");
            } else {
                var feedbackForm = feedbackFormRepository.findById(request.getFeedbackFormId())
                        .orElseThrow(() -> new RuntimeException("No feedback form with ID: " + request.getFeedbackFormId()));
                if(courses.contains(feedbackForm.getCourse().getName())){
                    var feedback = Feedback.builder().build();
                    feedback.setFeedbackForm(feedbackForm);
                    var questions = request.getQuestions().stream().map(q -> {
                        q.setFeedbackForm(feedbackForm);
                        q.setFeedback(feedback);
                        return q;
                    }).collect(Collectors.toList());
                    feedback.setQuestions(questions);
                    var saved = feedbackRepository.save(feedback);
                    return saved;
                } else{
                    throw new RuntimeException("Current Feedback doesn't exist for student's courses");
                }

            }
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
