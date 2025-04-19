package com.easybusy.api.services;

import com.easybusy.api.dto.CartDto;
import com.easybusy.api.dto.CartItemDto;

public interface CartService {
    
    CartDto getCartByUserId(Long userId);
    
    CartDto addItemToCart(Long userId, CartItemDto cartItemDto);
    
    CartDto updateCartItem(Long userId, Long cartItemId, CartItemDto cartItemDto);
    
    CartDto removeItemFromCart(Long userId, Long cartItemId);
    
    CartDto clearCart(Long userId);
    
    CartDto applyCoupon(Long userId, String couponCode);
    
    CartDto removeCoupon(Long userId);
}