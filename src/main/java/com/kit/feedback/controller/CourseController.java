package com.kit.feedback.controller;

import com.kit.feedback.dto.CourseRequest;
import com.kit.feedback.model.Course;
import com.kit.feedback.services.CourseService;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Course", description = "Course")
@RestController
@ApiIgnore
@RequestMapping("api/v1/course")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping("/create")
    public ResponseEntity createCourse(@RequestBody CourseRequest request){
        try{
            return ResponseEntity.ok(courseService.create(request));
        }catch(Exception e){
            return ResponseEntity.ok(e.getMessage());
        }
    }
    @PostMapping("/edit")
    public ResponseEntity editCourse(@RequestBody CourseRequest request){
        try{
            return ResponseEntity.ok(courseService.edit(request));
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/get")
    public ResponseEntity getCourse(@RequestParam UUID id){
        try{
            return ResponseEntity.ok(courseService.get(id));
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/delete")
    public ResponseEntity deleteCourse(@RequestParam UUID id){
        try{
            return ResponseEntity.ok(courseService.delete(id));
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/get-all")
    public ResponseEntity getAll(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size){
        try{
            return ResponseEntity.ok(courseService.getCourses(page, size));
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
