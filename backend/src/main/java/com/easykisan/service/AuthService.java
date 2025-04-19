package com.easykisan.service;

import com.easykisan.dto.JwtAuthResponse;
import com.easykisan.dto.UserDto;
import com.easykisan.dto.request.LoginRequest;
import com.easykisan.dto.request.RegisterRequest;

public interface AuthService {
    
    JwtAuthResponse login(LoginRequest loginRequest);
    
    UserDto register(RegisterRequest registerRequest);
    
    void logout(String token);
    
    JwtAuthResponse refreshToken(String refreshToken);
    
    boolean validateToken(String token);
}