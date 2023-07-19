package com.kit.feedback.services;

import com.kit.feedback.dto.CourseRequest;
import com.kit.feedback.dto.LecturersResponse;
import com.kit.feedback.dto.StudentRequest;
import com.kit.feedback.dto.StudentsResponse;
import com.kit.feedback.model.Student;
import com.kit.feedback.repository.CourseRepository;
import com.kit.feedback.repository.LecturerRepository;
import com.kit.feedback.repository.StudentRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public StudentsResponse getStudents(Integer page, Integer size){
        PageRequest pageRequest = PageRequest.of(page, size);
        var students = studentRepository.findAll(pageRequest);
        return StudentsResponse.builder()
                .content(students.getContent())
                .count(studentRepository.count())
                .build();
    }

    public Student get(UUID id){
        try {
            var student = studentRepository.findById(id).orElseThrow(() -> new RuntimeException("Cannot find student with Id: " + id));
            return student;
        } catch (Exception e) {
            
        }
        return null;
    }

    public Student assignCourse(StudentRequest request){
        var student = studentRepository.findById(request.getStudentId()).orElseThrow(() -> new RuntimeException("Cannot find student with ID: " + request.getStudentId()));
        var course = courseRepository.getCourseById(request.getCourseId()).orElseThrow(() -> new RuntimeException("Cannot find course with ID: " + request.getCourseId()));
        //Check if course is already assign to student
        var courses = student.getCourses();
        var courseExist = courses.contains(course);
        if(courseExist){
            throw new RuntimeException("Course already assigned to this student");
        } else{
            courses.add(course);
            student.setCourses(courses);
            var saved = studentRepository.save(student);
            return saved;
        }
    }
}
