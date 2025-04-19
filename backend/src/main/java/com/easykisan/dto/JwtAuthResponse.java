package com.easykisan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthResponse {
    private String tokenType;
    private String accessToken;
    private String refreshToken;
    private long expiresIn;
    private UserDto user;
}