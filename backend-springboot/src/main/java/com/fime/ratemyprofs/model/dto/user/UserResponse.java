package com.fime.ratemyprofs.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Integer userId;
    private String name;
    private String email;
    private String role;
    private LocalDateTime createdAt;
    private Long reviewCount;
}
