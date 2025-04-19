package com.easykisan.controller;

import com.easykisan.dto.response.ApiResponse;
import com.easykisan.model.User;
import com.easykisan.repository.UserRepository;
import com.easykisan.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Remove sensitive information like password before returning
        user.setPassword(null);
        
        return ResponseEntity.ok(ApiResponse.success("User profile retrieved successfully", user));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Remove sensitive information like password before returning
        user.setPassword(null);
        
        return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", user));
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", userRepository.findAll()));
    }
    
    @PutMapping("/me/deactivate")
    public ResponseEntity<?> deactivateAccount(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setActive(false);
        userRepository.save(user);
        
        return ResponseEntity.ok(ApiResponse.success("Account deactivated successfully"));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.badRequest().body(ApiResponse.error("User not found"));
        }
        
        userRepository.deleteById(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully"));
    }
}