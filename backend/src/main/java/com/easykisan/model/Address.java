package com.easykisan.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "addresses")
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private AddressType type = AddressType.SHIPPING;
    
    @NotBlank
    @Size(max = 100)
    private String addressLine1;
    
    @Size(max = 100)
    private String addressLine2;
    
    @NotBlank
    @Size(max = 100)
    private String city;
    
    @NotBlank
    @Size(max = 100)
    private String state;
    
    @NotBlank
    @Size(max = 20)
    private String postalCode;
    
    @NotBlank
    @Size(max = 100)
    private String country;
    
    private boolean isDefault = false;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
    
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    public enum AddressType {
        SHIPPING,
        BILLING
    }
}