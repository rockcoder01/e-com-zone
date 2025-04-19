package com.easykisan.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "carts")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"items", "user"})
public class Cart {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
    
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();
    
    private BigDecimal subtotal = BigDecimal.ZERO;
    
    private BigDecimal tax = BigDecimal.ZERO;
    
    private BigDecimal shipping = BigDecimal.ZERO;
    
    private BigDecimal discount = BigDecimal.ZERO;
    
    private BigDecimal total = BigDecimal.ZERO;
    
    private String couponCode;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private CartStatus status = CartStatus.ACTIVE;
    
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    public enum CartStatus {
        ACTIVE,
        ABANDONED,
        CONVERTED
    }
    
    // Helper method to add an item
    public void addItem(CartItem item) {
        items.add(item);
        item.setCart(this);
    }
    
    // Helper method to remove an item
    public void removeItem(CartItem item) {
        items.remove(item);
        item.setCart(null);
    }
    
    // Helper method to clear all items
    public void clearItems() {
        items.clear();
    }
    
    // Helper method to calculate totals
    public void calculateTotals() {
        // Calculate subtotal
        this.subtotal = items.stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Calculate tax (assuming a 8% tax rate for now)
        this.tax = this.subtotal.multiply(new BigDecimal("0.08"));
        
        // Calculate shipping (flat rate for now)
        this.shipping = items.isEmpty() ? BigDecimal.ZERO : new BigDecimal("5.99");
        
        // Ensure discount is not null
        if (this.discount == null) {
            this.discount = BigDecimal.ZERO;
        }
        
        // Calculate total
        this.total = this.subtotal.add(this.tax).add(this.shipping).subtract(this.discount);
    }
}