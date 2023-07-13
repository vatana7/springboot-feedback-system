package com.kit.feedback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@SpringBootApplication
@SecurityScheme(name = "BasicAuth", type = SecuritySchemeType.HTTP, scheme = "basic")
//@EnableSwagger2
public class StudentFeedbackSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(StudentFeedbackSystemApplication.class, args);
    }

}
