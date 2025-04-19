package com.easykisan.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @NotBlank
    @Size(max = 100)
    private String addressLine1;
    
    @Size(max = 100)
    private String addressLine2;
    
    @NotBlank
    @Size(max = 50)
    private String city;
    
    @NotBlank
    @Size(max = 50)
    private String state;
    
    @NotBlank
    @Size(max = 10)
    private String postalCode;
    
    @NotBlank
    @Size(max = 50)
    private String country;
    
    @Size(max = 20)
    private String phone;
    
    @Size(max = 50)
    private String fullName;
    
    private boolean isDefault;
    
    @Enumerated(EnumType.STRING)
    private AddressType addressType;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    public enum AddressType {
        BILLING,
        SHIPPING,
        BOTH
    }
}