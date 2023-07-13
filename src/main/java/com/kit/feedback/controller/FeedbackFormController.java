package com.kit.feedback.controller;

import com.kit.feedback.dto.FeedbackFormRequest;
import com.kit.feedback.model.FeedbackForm;
import com.kit.feedback.services.FeedbackFormService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/feedback-form")
@RequiredArgsConstructor
public class FeedbackFormController {
    private final FeedbackFormService feedbackFormService;
    @PostMapping(value = "/create")
    public ResponseEntity createFeedbackForm(@RequestBody FeedbackFormRequest request){
        try{
            return ResponseEntity.ok(feedbackFormService.create(request));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/get-all")
    public ResponseEntity createFeedbackForm(@RequestParam Integer page, @RequestParam Integer size){
        try{
            return ResponseEntity.ok(feedbackFormService.getAll(page, size));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
