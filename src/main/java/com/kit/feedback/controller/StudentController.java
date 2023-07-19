package com.kit.feedback.controller;

import com.kit.feedback.dto.StudentRequest;
import com.kit.feedback.services.StudentService;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Student", description = "Student")
@RestController
@ApiIgnore
@RequestMapping(value = "/api/v1/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/get-all-student")
    public ResponseEntity getAllStudents(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size){
        try{
            return ResponseEntity.ok(studentService.getStudents(page, size));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get")
    public ResponseEntity findStudentById(@RequestParam UUID id){
        try {
            return ResponseEntity.ok(studentService.get(id));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/assign-course-to-student")
    public ResponseEntity assignCourseToStudent(@RequestBody StudentRequest request){
        try{
            return ResponseEntity.ok(studentService.assignCourse(request));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
