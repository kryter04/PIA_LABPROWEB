package com.fime.ratemyprofs.model.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecoveryTokenInfo {

    private Integer tokenId;
    private String token;
    private Integer userId;
    private String userEmail;
    private String userName;
    private LocalDateTime expiresAt;
    private Boolean used;
    private Boolean expired;
    private Boolean valid;
    private LocalDateTime createdAt;
}
