package com.easykisan.service.impl;

import com.easykisan.dto.UserDto;
import com.easykisan.dto.request.ChangePasswordRequest;
import com.easykisan.dto.request.UpdateUserRequest;
import com.easykisan.exception.ResourceNotFoundException;
import com.easykisan.exception.BadRequestException;
import com.easykisan.model.User;
import com.easykisan.repository.UserRepository;
import com.easykisan.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public UserDto getUserById(Long id) {
        return convertToDto(getUserEntityById(id));
    }
    
    @Override
    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        return convertToDto(user);
    }
    
    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return convertToDto(user);
    }
    
    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public UserDto updateUser(Long id, UpdateUserRequest updateUserRequest) {
        User user = getUserEntityById(id);
        
        if (updateUserRequest.getEmail() != null && 
                !updateUserRequest.getEmail().equals(user.getEmail()) && 
                userRepository.existsByEmail(updateUserRequest.getEmail())) {
            throw new BadRequestException("Email already in use");
        }
        
        if (updateUserRequest.getFirstName() != null) {
            user.setFirstName(updateUserRequest.getFirstName());
        }
        
        if (updateUserRequest.getLastName() != null) {
            user.setLastName(updateUserRequest.getLastName());
        }
        
        if (updateUserRequest.getEmail() != null) {
            user.setEmail(updateUserRequest.getEmail());
        }
        
        if (updateUserRequest.getPhone() != null) {
            user.setPhone(updateUserRequest.getPhone());
        }
        
        if (updateUserRequest.getProfileImage() != null) {
            user.setProfileImage(updateUserRequest.getProfileImage());
        }
        
        if (updateUserRequest.getDefaultShippingAddressId() != null) {
            user.setDefaultShippingAddressId(updateUserRequest.getDefaultShippingAddressId());
        }
        
        if (updateUserRequest.getDefaultBillingAddressId() != null) {
            user.setDefaultBillingAddressId(updateUserRequest.getDefaultBillingAddressId());
        }
        
        user.setUpdatedAt(LocalDateTime.now());
        
        return convertToDto(userRepository.save(user));
    }
    
    @Override
    @Transactional
    public void changePassword(Long id, ChangePasswordRequest changePasswordRequest) {
        User user = getUserEntityById(id);
        
        if (!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password is incorrect");
        }
        
        if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword())) {
            throw new BadRequestException("New password and confirm password do not match");
        }
        
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        
        userRepository.save(user);
    }
    
    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        
        userRepository.deleteById(id);
    }
    
    @Override
    public boolean isEmailTaken(String email) {
        return userRepository.existsByEmail(email);
    }
    
    @Override
    public boolean isUsernameTaken(String username) {
        return userRepository.existsByUsername(username);
    }
    
    @Override
    public User getUserEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }
    
    @Override
    @Transactional
    public void updateUserLastLogin(Long id) {
        User user = getUserEntityById(id);
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);
    }
    
    @Override
    @Transactional
    public void verifyEmail(String token) {
        // Implementation will require token verification logic
        // Placeholder for now, will implement with actual token verification
        throw new UnsupportedOperationException("Email verification not yet implemented");
    }
    
    @Override
    @Transactional
    public void requestPasswordReset(String email) {
        // Implementation will require generating reset token and sending email
        // Placeholder for now, will implement with actual reset logic
        throw new UnsupportedOperationException("Password reset request not yet implemented");
    }
    
    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {
        // Implementation will require token verification and password update
        // Placeholder for now, will implement with actual reset logic
        throw new UnsupportedOperationException("Password reset not yet implemented");
    }
    
    private UserDto convertToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .profileImage(user.getProfileImage())
                .roles(user.getRoles().stream()
                        .map(role -> role.getName().name())
                        .collect(Collectors.toSet()))
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .isActive(user.isActive())
                .isEmailVerified(user.isEmailVerified())
                .defaultShippingAddressId(user.getDefaultShippingAddressId())
                .defaultBillingAddressId(user.getDefaultBillingAddressId())
                .build();
    }
}