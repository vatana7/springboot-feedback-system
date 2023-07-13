package com.kit.feedback.services;

import com.kit.feedback.dto.LecturersResponse;
import com.kit.feedback.repository.LecturerRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LecturerService {
    private final LecturerRepository lecturerRepository;

    public LecturersResponse getLecturers(Integer page, Integer size){
        PageRequest pageRequest = PageRequest.of(page, size);
        var lecturers = lecturerRepository.findAll(pageRequest);
        return LecturersResponse.builder()
                .contents(lecturers.getContent())
                .count(lecturerRepository.count())
                .build();
    }
}
