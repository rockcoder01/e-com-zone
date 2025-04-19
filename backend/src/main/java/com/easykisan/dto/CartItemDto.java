package com.easykisan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {
    private Long id;
    private Long productId;
    private String productName;
    private String productImage;
    private double price;
    private int quantity;
    private double subtotal;
    private String sku;
    private boolean inStock;
    private int maxQuantity;
}