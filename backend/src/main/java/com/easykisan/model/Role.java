package com.easykisan.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Entity
@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
public class Role implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20, unique = true)
    private ERole name;
    
    public Role(ERole name) {
        this.name = name;
    }
    
    public enum ERole {
        ROLE_USER,
        ROLE_ADMIN,
        ROLE_VENDOR,
        ROLE_INFLUENCER
    }
}