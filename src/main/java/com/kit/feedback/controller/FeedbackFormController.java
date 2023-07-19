package com.kit.feedback.controller;

import com.kit.feedback.dto.FeedbackFormRequest;
import com.kit.feedback.dto.FeedbackRequest;
import com.kit.feedback.model.FeedbackForm;
import com.kit.feedback.services.FeedbackFormService;

import com.kit.feedback.services.FeedbackService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "FeedbackForm", description = "FeedbackForm")
@RestController
@ApiIgnore
@RequestMapping("/api/v1/feedback-form")
@RequiredArgsConstructor
public class FeedbackFormController {

    private final FeedbackFormService feedbackFormService;
    private final FeedbackService feedbackService;

    @PostMapping(value = "/create")
    public ResponseEntity createFeedbackForm(@RequestBody FeedbackFormRequest request){
        try{
            return ResponseEntity.ok(feedbackFormService.create(request));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/submit")
    public ResponseEntity submitFeeback(@RequestBody FeedbackRequest request){
        try{
            return ResponseEntity.ok(feedbackService.saveFeedback(request));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/get")
    public ResponseEntity getFeedbackForm(@RequestParam Integer id){
        try{
            return ResponseEntity.ok(feedbackFormService.getById(id));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/edit")
    public ResponseEntity editFeedbackForm(@RequestBody FeedbackFormRequest request){
        try {
            return ResponseEntity.ok(feedbackFormService.edit(request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/get-all")
    public ResponseEntity getAllFeedbackForms(@RequestParam Integer page, @RequestParam Integer size){
        try{
            return ResponseEntity.ok(feedbackFormService.getAll(page, size));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/get-response")
    public ResponseEntity getResponses(){
        try{
            return ResponseEntity.ok(feedbackFormService.getResponse());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/get-specific-response")
    public ResponseEntity getResponses(@RequestParam(name = "id") UUID courseId){
        try{
            return ResponseEntity.ok(feedbackFormService.getSpecificResponse(courseId));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/delete")
    public ResponseEntity deleteFeedbackForm(@RequestParam Integer id){
        try{
            return ResponseEntity.ok(feedbackFormService.delete(id));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
