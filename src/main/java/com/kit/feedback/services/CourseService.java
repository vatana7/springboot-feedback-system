package com.kit.feedback.services;

import com.kit.feedback.dto.CourseRequest;
import com.kit.feedback.dto.CoursesResonse;
import com.kit.feedback.model.Course;
import com.kit.feedback.model.Lecturer;
import com.kit.feedback.model.User;
import com.kit.feedback.repository.CourseRepository;
import com.kit.feedback.repository.LecturerRepository;
import com.kit.feedback.repository.SemesterRepository;
import com.kit.feedback.repository.UserRepository;
import com.kit.feedback.utils.Utility;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.asm.Advice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final SemesterRepository semesterRepository;
    private final LecturerRepository lecturerRepository;

    public CoursesResonse getCourses(Integer page, Integer size){
        PageRequest pageRequest = PageRequest.of(page, size);
        var courses = courseRepository.findAll(pageRequest);
        return CoursesResonse.builder()
                .content(courses.getContent())
                .count(courseRepository.count())
                .build();
    }

    public Course create(CourseRequest request){
        var user = (User)Utility.getCurrentAuthentication().getPrincipal();
        var lecturer = lecturerRepository.findById(request.getLecturerId()).orElseThrow(() -> new RuntimeException("Lecturer not found, ID: " + request.getLecturerId()));
        //Check if Course is already exist
        var course = courseRepository.findByName(request.getName());
        if(course.isPresent()){
            throw new RuntimeException("Course already exist: " + request.getName());
        }
        //Check if Semester exists
        var semester = semesterRepository.getSemesterById(request.getSemesterId())
                .orElseThrow(() -> new RuntimeException("Semester Id doesn't exist: " + request.getSemesterId().toString()));
        var newCourse = Course.builder().build();
        newCourse.setCredit(request.getCredit());
        newCourse.setName(request.getName());
        newCourse.setSemester(semester);
        newCourse.setCreatedBy(user.getId());
        newCourse.setCreatedAt(LocalDateTime.now().toString());
        newCourse.setUpdatedAt(LocalDateTime.now().toString());
        newCourse.setUpdatedBy(user.getId());
        newCourse.setLecturer(lecturer);
        if(Utility.checkIfAdmin()){
            //Save new Course
            var saved = courseRepository.save(newCourse);

            //Check if course already exist in current lecturer
            var courses = lecturer.getCourses().stream().filter(item -> item.getName().equals(request.getName())).collect(Collectors.toList());
            if(!courses.isEmpty()){
                throw new RuntimeException("Current Course already exist for current lecturer: Course:" + request.getName());
            }else{
                //Add course to courseList
                var courseList = lecturer.getCourses();
                courseList.add(newCourse);
                lecturer.setCourses(courseList);
                lecturerRepository.save(lecturer);
            }
            return saved;
        } else{
            throw new RuntimeException("Failed to create, Reason: Not Admin");
        }
    }

    public Course get(UUID id){
        var course = courseRepository.getCourseById(id).orElseThrow(() -> new RuntimeException("Cannot find course with ID: " + id.toString()));
        return course;
    }

    public Course edit(CourseRequest request){
        var user = (User)Utility.getCurrentAuthentication().getPrincipal();
        var lecturer = lecturerRepository.findById(request.getLecturerId()).orElseThrow(() -> new RuntimeException("Lecturer Id doesn't exist, ID: " + request.getLecturerId()));
        var course = courseRepository.getCourseById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course doesn't exist: ID:" + request.getCourseId()));
        course.setName(request.getName());
        course.setCredit(request.getCredit());
        course.setUpdatedBy(user.getId());
        course.setUpdatedAt(LocalDateTime.now().toString());
        course.setLecturer(lecturer);
        if(Utility.checkIfAdmin()){
            //Save Course
            var saved = courseRepository.save(course);

            //Save Course to lecturer
            addAndRemoveCourseFromLecturer(request, course, lecturer);
            return saved;
        }else{
            throw new RuntimeException("Failed to edit, Reason: Not Admin");
        }
    }

    public String delete(UUID id){
        try{
            if(Utility.checkIfAdmin()){
                courseRepository.deleteById(id);
                return "Successfully delete course ID: " + id;
            }else{
                return "Failed to delete, Reason: Not Admin";
            }
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    private void  addAndRemoveCourseFromLecturer(CourseRequest request, Course course, Lecturer lecturer){
        //Check if same lecturer or different
        if(request.getLecturerId() != course.getLecturer().getId()){
            //Check if Current Lecturer have current course
            var courses = lecturer.getCourses();
            var exist = courses.stream().filter(item -> item.getName().equals(request.getName())).collect(Collectors.toList());
            if(!exist.isEmpty()){
                //No need to add because already exist
                lecturerRepository.save(lecturer);
            }else{
                courses.add(course);
                lecturer.setCourses(courses);

                //Remove course from previous lecturer
                var prevLecturer = course.getLecturer();
                var prevCourses = prevLecturer.getCourses();
                var removedCourse = prevCourses.stream().filter(item -> item.getName().equals(request.getName())).collect(Collectors.toList());
                prevLecturer.setCourses(removedCourse);
                lecturerRepository.save(prevLecturer);
                lecturerRepository.save(lecturer);
            }
        }
    }
}
