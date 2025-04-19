package com.easykisan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
    private Long id;
    private Long userId;
    private List<CartItemDto> items;
    private double subtotal;
    private double tax;
    private double shippingCost;
    private double discount;
    private double total;
    private int itemCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}