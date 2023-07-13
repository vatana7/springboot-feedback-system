package com.kit.feedback.controller;

import com.kit.feedback.services.LecturerService;
import com.kit.feedback.services.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/lecturer")
@RequiredArgsConstructor
public class LecturerController {
    private final LecturerService lecturerService;
    private final StudentService studentService;

    @GetMapping("/get-all-lecturer")
    public ResponseEntity getAllLecturers(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size){
        try{
            return ResponseEntity.ok(lecturerService.getLecturers(page, size));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
