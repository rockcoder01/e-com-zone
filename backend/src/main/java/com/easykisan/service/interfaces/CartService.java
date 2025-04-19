package com.easykisan.service.interfaces;

import com.easykisan.dto.request.CartRequest;
import com.easykisan.dto.response.CartResponse;

public interface CartService {
    
    /**
     * Get cart by user ID
     * @param userId User ID
     * @return CartResponse
     */
    CartResponse getCartByUserId(Long userId);
    
    /**
     * Add item to cart
     * @param userId User ID
     * @param cartRequest Cart item request
     * @return Updated cart
     */
    CartResponse addItemToCart(Long userId, CartRequest cartRequest);
    
    /**
     * Update cart item
     * @param userId User ID
     * @param itemId Cart item ID
     * @param cartRequest Cart item request
     * @return Updated cart
     */
    CartResponse updateCartItem(Long userId, Long itemId, CartRequest cartRequest);
    
    /**
     * Remove item from cart
     * @param userId User ID
     * @param itemId Cart item ID
     * @return Updated cart
     */
    CartResponse removeItemFromCart(Long userId, Long itemId);
    
    /**
     * Clear all items from cart
     * @param userId User ID
     * @return Empty cart
     */
    CartResponse clearCart(Long userId);
    
    /**
     * Apply coupon to cart
     * @param userId User ID
     * @param couponCode Coupon code
     * @return Updated cart
     */
    CartResponse applyCoupon(Long userId, String couponCode);
    
    /**
     * Remove coupon from cart
     * @param userId User ID
     * @return Updated cart
     */
    CartResponse removeCoupon(Long userId);
}