package com.kit.feedback.services;

import com.kit.feedback.dto.AuthenticationRequest;
import com.kit.feedback.dto.AuthenticationResponse;
import com.kit.feedback.dto.RegisterRequest;
import com.kit.feedback.dto.ResetPasswordRequest;
import com.kit.feedback.enums.Role;
import com.kit.feedback.model.*;
import com.kit.feedback.repository.LecturerRepository;
import com.kit.feedback.repository.ResetPasswordRepository;
import com.kit.feedback.repository.StudentRepository;
import com.kit.feedback.repository.UserRepository;
import com.kit.feedback.utils.Utility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ResetPasswordRepository resetPasswordRepository;
    private final MailSenderService mailService;
    private final LecturerRepository lecturerRepository;
    private final StudentRepository studentRepository;

    public AuthenticationResponse register(RegisterRequest request){
        if(userRepository.existsByEmailOrUsername(request.getEmail(), request.getUsername())){
            throw new RequestRejectedException("Username or email is already used");
        }
        try{
            var user = User
                    .builder()
                    .username(request.getUsername())
                    .name(request.getFirstname() + " " + request.getLastname())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(request.getRole())
                    .firstname(request.getFirstname())
                    .lastname(request.getLastname())
                    .build();
            //Save user to STUDENT, LECTURER table
            UUID id= null;
            switch (request.getRole()){
                case STUDENT -> {
                    var saved = studentRepository.save(Student.builder()
                            .name(user.getName())
                            .user(user)
                            .build());
                    user.setStudent(saved);
                    id = saved.getId();
                }
                case LECTURER -> {
                    var saved = lecturerRepository.save(Lecturer.builder()
                            .name(user.getName())
                            .user(user)
                            .build());
                    user.setLecturer(saved);
                    id = saved.getId();
                }
            }
            var postUser = userRepository.save(user);
            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .lastname(request.getLastname())
                    .firstname(request.getFirstname())
                    .role(request.getRole())
                    .email(request.getEmail())
                    .username(request.getUsername())
                    .accountId(postUser.getId())
                    .userId(id)
                    .build();
        } catch (Exception e){
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var role = user.getRole();
        UUID id;
        if(role.equals(Role.STUDENT)){
            id = studentRepository.findById(user.getStudent().getId()).orElseThrow().getId();
        } else if(role.equals(Role.LECTURER)){
            id = lecturerRepository.findById(user.getLecturer().getId()).orElseThrow().getId();
        } else {
            id = user.getId();
        }
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .lastname(user.getLastname())
                .firstname(user.getFirstname())
                .role(user.getRole())
                .email(user.getEmail())
                .username(user.getUsername())
                .accountId(user.getId())
                .userId(id)
                .build();
    }

    public String forgetPassword(String email){

        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        String resetToken = Utility.generateToken();
        ResetPassword resetPassword = new ResetPassword();
        resetPassword.setToken(resetToken);
        resetPassword.setExpiryDate(LocalDateTime.now().plusHours(24)); //Expire 24 from now
        resetPassword.setUser(user);

        resetPasswordRepository.save(resetPassword);
        String mailBody = "Password Reset Token: " + resetToken;
        //Send link to reset email
        mailService.sendNewMail(email, "Password reset for Student Feedback System", mailBody);
        return "Password Reset Link has been sent to email";
    }

    public String resetPassword(ResetPasswordRequest request){
        ResetPassword resetPassword = resetPasswordRepository.findByToken(request.getToken()).orElseThrow();

        if (resetPassword.getExpiryDate().isBefore(LocalDateTime.now())){
            return "Token has expired";
        }
        User user = resetPassword.getUser();
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        //Delete the used token from database
        resetPasswordRepository.deleteByToken(request.getToken());

        return "Password reset successfully";
    }
}
