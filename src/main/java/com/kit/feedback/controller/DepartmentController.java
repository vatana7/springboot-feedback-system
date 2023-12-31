package com.kit.feedback.controller;

import com.kit.feedback.dto.DepartmentsResponse;
import com.kit.feedback.model.Department;
import com.kit.feedback.dto.DepartmentResponse;
import com.kit.feedback.services.DepartmentService;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;
import springfox.documentation.annotations.ApiIgnore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Department", description = "Department")
@RestController
@ApiIgnore
@RequestMapping("/api/v1/department")
public class DepartmentController {
    private final DepartmentService departmentService;
    @Autowired
    public DepartmentController(DepartmentService departmentService){
        this.departmentService = departmentService;
    }
    @PostMapping("/create")
    public ResponseEntity createDepartment(@RequestBody Department request){
        try{
            return ResponseEntity.ok(departmentService.create(request));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get-all")
    public ResponseEntity<DepartmentsResponse> getAllDepartment(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ){
        return ResponseEntity.ok(departmentService.getDepartments(page, size));
    }

    @PostMapping("/edit")
    public ResponseEntity editDepartment(@RequestBody Department request){
        try{
            return ResponseEntity.ok(departmentService.edit(request));
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get")
    public ResponseEntity<DepartmentResponse> viewDepartment(@RequestParam(name = "id") UUID id){
        return ResponseEntity.ok(departmentService.get(id));
    }

    @GetMapping(value = "/delete")
    public ResponseEntity deleteDepartment(@RequestParam(name = "id") UUID id){
        try{
            return ResponseEntity.ok(departmentService.delete(id));
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
