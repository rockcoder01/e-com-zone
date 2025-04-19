package com.easykisan.service;

import com.easykisan.dto.UserDto;
import com.easykisan.dto.request.ChangePasswordRequest;
import com.easykisan.dto.request.UpdateUserRequest;
import com.easykisan.model.User;

import java.util.List;

public interface UserService {
    
    UserDto getUserById(Long id);
    
    UserDto getUserByUsername(String username);
    
    UserDto getUserByEmail(String email);
    
    List<UserDto> getAllUsers();
    
    UserDto updateUser(Long id, UpdateUserRequest updateUserRequest);
    
    void changePassword(Long id, ChangePasswordRequest changePasswordRequest);
    
    void deleteUser(Long id);
    
    boolean isEmailTaken(String email);
    
    boolean isUsernameTaken(String username);
    
    User getUserEntityById(Long id);
    
    void updateUserLastLogin(Long id);
    
    void verifyEmail(String token);
    
    void requestPasswordReset(String email);
    
    void resetPassword(String token, String newPassword);
}