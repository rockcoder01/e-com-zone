package com.easykisan.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(mappedBy = "cart")
    private User user;
    
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();
    
    @Column(precision = 10, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal tax = BigDecimal.ZERO;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal shipping = BigDecimal.ZERO;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal discount = BigDecimal.ZERO;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;
    
    private String couponCode;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    @Enumerated(EnumType.STRING)
    private CartStatus status = CartStatus.ACTIVE;
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    // Helper method to calculate totals
    public void calculateTotals() {
        this.subtotal = items.stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Typically tax, shipping, and discount would be calculated based on business rules
        // This is a simplified calculation
        this.total = this.subtotal.add(this.tax).add(this.shipping).subtract(this.discount);
    }
    
    // Helper method to add a cart item
    public void addItem(CartItem item) {
        items.add(item);
        item.setCart(this);
        calculateTotals();
    }
    
    // Helper method to remove a cart item
    public void removeItem(CartItem item) {
        items.remove(item);
        item.setCart(null);
        calculateTotals();
    }
    
    // Helper method to clear all items
    public void clearItems() {
        items.clear();
        calculateTotals();
    }
    
    public enum CartStatus {
        ACTIVE,
        CHECKOUT,
        ABANDONED,
        COMPLETED
    }
}