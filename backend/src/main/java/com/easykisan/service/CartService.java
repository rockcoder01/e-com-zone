package com.easykisan.service;

import com.easykisan.dto.CartDto;
import com.easykisan.dto.request.AddToCartRequest;
import com.easykisan.dto.request.UpdateCartItemRequest;

public interface CartService {
    
    CartDto getCartByUserId(Long userId);
    
    CartDto addToCart(Long userId, AddToCartRequest addToCartRequest);
    
    CartDto updateCartItem(Long userId, Long productId, UpdateCartItemRequest updateCartItemRequest);
    
    CartDto removeFromCart(Long userId, Long productId);
    
    void clearCart(Long userId);
    
    CartDto getOrCreateCart(Long userId);
}