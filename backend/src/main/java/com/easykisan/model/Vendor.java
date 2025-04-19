package com.easykisan.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "vendors")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"products"})
public class Vendor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Size(max = 100)
    private String name;
    
    @Size(max = 100)
    @Column(unique = true)
    private String slug;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    private String logoUrl;
    
    private String bannerUrl;
    
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;
    
    @Size(max = 15)
    private String phone;
    
    @Size(max = 255)
    private String address;
    
    @Size(max = 100)
    private String city;
    
    @Size(max = 100)
    private String state;
    
    @Size(max = 20)
    private String postalCode;
    
    @Size(max = 100)
    private String country;
    
    private Double rating = 0.0;
    
    private Integer reviewCount = 0;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL)
    private Set<Product> products = new HashSet<>();
    
    private boolean isActive = true;
    
    private boolean isVerified = false;
    
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
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