package com.easykisan.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vendors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vendor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Size(max = 100)
    private String name;
    
    @NotBlank
    @Size(max = 100)
    @Column(unique = true)
    private String slug;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Size(max = 255)
    private String logoUrl;
    
    @Size(max = 255)
    private String bannerUrl;
    
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;
    
    @Size(max = 20)
    private String phone;
    
    @Size(max = 255)
    private String address;
    
    @Size(max = 50)
    private String city;
    
    @Size(max = 50)
    private String state;
    
    @Size(max = 10)
    private String postalCode;
    
    @Size(max = 50)
    private String country;
    
    private Double rating = 0.0;
    
    private Integer reviewCount = 0;
    
    @Column(columnDefinition = "TEXT")
    private String returnPolicy;
    
    @Column(columnDefinition = "TEXT")
    private String shippingPolicy;
    
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products = new ArrayList<>();
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private boolean isActive = true;
    
    private boolean isVerified = false;
    
    private boolean isFeatured = false;
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    // Helper method to add a product
    public void addProduct(Product product) {
        products.add(product);
        product.setVendor(this);
    }
    
    // Helper method to remove a product
    public void removeProduct(Product product) {
        products.remove(product);
        product.setVendor(null);
    }
}