package com.kit.feedback.dto;

import com.kit.feedback.model.User;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsersResponse {
    private List<UserResponse> content;
    private Integer count;
}
