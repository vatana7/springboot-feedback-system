package com.kit.feedback.controller;

import com.kit.feedback.model.Department;
import com.kit.feedback.services.AuthenticationService;
import com.kit.feedback.services.DepartmentService;
import com.kit.feedback.services.MailSenderService;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Home", description = "Home")
@RestController
@ApiIgnore
@RequestMapping("/api/v1/demo")
@RequiredArgsConstructor
public class HomeController {

    @Autowired
    private MailSenderService javaMailSender;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private AuthenticationService authService;
    @RequestMapping(value = {"/home"}, method = RequestMethod.POST)
    public ResponseEntity<String> displayHome(@RequestBody Department request) {
        return ResponseEntity.ok("Home at secured endpoint");
    }

}
