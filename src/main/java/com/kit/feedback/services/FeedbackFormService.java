package com.kit.feedback.services;

import com.kit.feedback.dto.CourseResponse;
import com.kit.feedback.dto.FeedbackFormRequest;
import com.kit.feedback.dto.FeedbackFormsResponse;
import com.kit.feedback.dto.FeedbackResult;
import com.kit.feedback.enums.Role;
import com.kit.feedback.model.*;
import com.kit.feedback.repository.CourseRepository;
import com.kit.feedback.repository.FeedbackFormRepository;
import com.kit.feedback.repository.QuestionRepository;
import com.kit.feedback.utils.Utility;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedbackFormService {
    private final FeedbackFormRepository feedbackFormRepository;
    private final CourseRepository courseRepository;
    private final QuestionRepository questionRepository;

    private final EntityManager entityManager;

    public FeedbackForm getById(Integer id){
        return feedbackFormRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cannot find feedback form, ID: " + id));
    }

    public FeedbackForm create(FeedbackFormRequest request){
        var user = (User)Utility.getCurrentAuthentication().getPrincipal();
        if(user.getRole().equals(Role.ADMIN)){
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
        else {
            throw new RuntimeException("Not admin, cannot create form");
        }
    }

    public FeedbackFormsResponse getAll(Integer page, Integer size){
        var user = (User)Utility.getCurrentAuthentication().getPrincipal();
        PageRequest pageRequest = PageRequest.of(page, size);
        var feedbackForms = feedbackFormRepository.findAll(pageRequest);

        //Show feedbackForms that exist for student only
        if(user.getRole().equals(Role.STUDENT)) {
            var courses = user.getStudent().getCourses().stream().map(item -> item.getName()).collect(Collectors.toList());
            var res = feedbackForms.getContent()
                    .stream()
                    .filter(feedbackForm -> courses.contains(feedbackForm.getCourse().getName()))
                    .collect(Collectors.toList());
            return FeedbackFormsResponse.builder()
                    .content(res)
                    .count(feedbackFormRepository.count())
                    .build();
            } else if(user.getRole().equals(Role.ADMIN)) {
                return FeedbackFormsResponse.builder()
                        .content(feedbackForms.getContent())
                        .count(feedbackFormRepository.count())
                        .build();
            }
        return null;
        }

    public FeedbackForm edit(FeedbackFormRequest request){
        var user = (User)Utility.getCurrentAuthentication().getPrincipal();
        if(user.getRole().equals(Role.ADMIN)){
            var feedbackForm = feedbackFormRepository.findById(request.getFeedbackFormId())
                    .orElseThrow(() -> new RuntimeException("Cannot find feedback form, ID: " + request.getFeedbackFormId()));
            feedbackForm.setTitle(request.getTitle());
            feedbackForm.setDescription(request.getDescription());
            feedbackForm.setQuestions(request.getQuestions());
            feedbackForm.setUpdatedAt(LocalDateTime.now().toString());
            feedbackForm.setUpdatedBy(user.getId());
            try {
                var saved = feedbackFormRepository.save(feedbackForm);
                return saved;
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        else {
            throw new RuntimeException("Cannot edit because not admin");
        }
    }

    public String delete(Integer id){
        try{
            var user = (User)Utility.getCurrentAuthentication().getPrincipal();
            if(user.getRole().equals(Role.ADMIN)){
                feedbackFormRepository.deleteById(id);
                return "Delete FeedbackForm successfully, ID: " + id;
            } else{
                throw new RuntimeException("Cannot delete because not admin");
            }

        }catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<FeedbackResult> getSpecificResponse(UUID courseId){
        var course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("No course Id: " + courseId));
        var feedbackFormId = course.getFeedbackForms().get(0).getId();
//        var result = questionRepository.findResponseById(feedbackFormId);
        String sql = "SELECT question_text, question_number, rating, COUNT(*) AS count FROM question WHERE feedback_form_id = " + feedbackFormId + " GROUP BY question_text,question_number, rating ORDER BY question_text,question_number, rating ASC";

        List<Object[]> results = entityManager.createNativeQuery(sql).getResultList();

        List<FeedbackResult> feedbackResults = new ArrayList<>();

        for (Object[] result : results) {
            String questionText = ((String) result[0]);
            Integer questionNumber = (Integer) result[1];
            Integer rating = (Integer) result[2];
            Long count = ((Number) result[3]).longValue();

            FeedbackResult feedbackResult = new FeedbackResult(questionNumber, rating, count, questionText);
            feedbackResults.add(feedbackResult);
        }
        return feedbackResults;
    }

    public List<CourseResponse> getResponse(){
        var user = (User)Utility.getCurrentAuthentication().getPrincipal();
        var role = user.getRole();
        List<CourseResponse> courseResponses = new ArrayList<>();
        if (role.equals(Role.STUDENT)){
            var studentCourses = user.getStudent().getCourses();
            studentCourses.forEach(item -> {

                //Get total vote in that course
                var getCourse = courseRepository.findById(item.getId()).orElseThrow(() -> new RuntimeException("No course Id: " + item.getId()));

                if (getCourse.getFeedbackForms().isEmpty()) {;} else {
                    var feedbackFormId = getCourse.getFeedbackForms().get(0).getId();
                    var sql = "SELECT Count(rating) AS total_rating_count FROM question WHERE feedback_form_id = " + feedbackFormId + " GROUP BY feedback_form_id";

                    int totalVote = 0;
                    try {
                        var count = entityManager.createNativeQuery(sql).getSingleResult();
                        totalVote = ((Number) count).intValue();
                    } catch (Exception e) {
                        // Handle the error gracefully, for example, log the error
                        System.err.println("Error executing SQL query: " + e.getMessage());
                    }

                    var course = CourseResponse.builder()
                            .id(item.getId())
                            .name(item.getName())
                            .lecturerName(item.getLecturer().getName())
                            .totalVote(totalVote)
                            .build();
                    courseResponses.add(course);
                }
            });
            return courseResponses;
        } else if (role.equals(Role.LECTURER)){
            var courses = user.getLecturer().getCourses();
            courses.forEach(item -> {

                //Get total vote in that course
                var getCourse = courseRepository.findById(item.getId()).orElseThrow(() -> new RuntimeException("No course Id: " + item.getId()));

                if (getCourse.getFeedbackForms().isEmpty()) {;} else {
                    var feedbackFormId = getCourse.getFeedbackForms().get(0).getId();
                    var sql = "SELECT Count(rating) AS total_rating_count FROM question WHERE feedback_form_id = " + feedbackFormId + " GROUP BY feedback_form_id";

                    int totalVote = 0;
                    try {
                        var count = entityManager.createNativeQuery(sql).getSingleResult();
                        totalVote = ((Number) count).intValue();
                    } catch (Exception e) {
                        // Handle the error gracefully, for example, log the error
                        System.err.println("Error executing SQL query: " + e.getMessage());
                    }

                    var course = CourseResponse.builder()
                            .id(item.getId())
                            .name(item.getName())
                            .lecturerName(item.getLecturer().getName())
                            .totalVote(totalVote)
                            .build();
                    courseResponses.add(course);
                }
            });
            return courseResponses;
        } else {
            var courses =  courseRepository.findAll();
            courses.forEach(item -> {

                var getCourse = courseRepository.findById(item.getId()).orElseThrow(() -> new RuntimeException("No course Id: " + item.getId()));

                if (getCourse.getFeedbackForms().isEmpty()) {;} else {
                    var feedbackFormId = getCourse.getFeedbackForms().get(0).getId();
                    var sql = "SELECT Count(rating) AS total_rating_count FROM question WHERE feedback_form_id = " + feedbackFormId + " GROUP BY feedback_form_id";

                    int totalVote = 0;
                    try {
                        var count = entityManager.createNativeQuery(sql).getSingleResult();
                        totalVote = ((Number) count).intValue();
                    } catch (Exception e) {
                        // Handle the error gracefully, for example, log the error
                        System.err.println("Error executing SQL query: " + e.getMessage());
                    }

                    var course = CourseResponse.builder()
                            .id(item.getId())
                            .name(item.getName())
                            .lecturerName(item.getLecturer().getName())
                            .totalVote(totalVote)
                            .build();
                    courseResponses.add(course);
                }
            });
            return courseResponses;
        }
    }
}
