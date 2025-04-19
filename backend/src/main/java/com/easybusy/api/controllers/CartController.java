package com.easybusy.api.controllers;

import com.easybusy.api.dto.CartDto;
import com.easybusy.api.dto.CartItemDto;
import com.easybusy.api.services.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<CartDto> getCart(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = extractUserId(userDetails);
        CartDto cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/items")
    public ResponseEntity<CartDto> addItemToCart(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CartItemDto cartItemDto) {
        Long userId = extractUserId(userDetails);
        CartDto updatedCart = cartService.addItemToCart(userId, cartItemDto);
        return new ResponseEntity<>(updatedCart, HttpStatus.CREATED);
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<CartDto> updateCartItem(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long itemId,
            @Valid @RequestBody CartItemDto cartItemDto) {
        Long userId = extractUserId(userDetails);
        CartDto updatedCart = cartService.updateCartItem(userId, itemId, cartItemDto);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<CartDto> removeCartItem(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long itemId) {
        Long userId = extractUserId(userDetails);
        CartDto updatedCart = cartService.removeItemFromCart(userId, itemId);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<CartDto> clearCart(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = extractUserId(userDetails);
        CartDto emptyCart = cartService.clearCart(userId);
        return ResponseEntity.ok(emptyCart);
    }

    @PostMapping("/coupon")
    public ResponseEntity<CartDto> applyCoupon(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam String couponCode) {
        Long userId = extractUserId(userDetails);
        CartDto updatedCart = cartService.applyCoupon(userId, couponCode);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/coupon")
    public ResponseEntity<CartDto> removeCoupon(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = extractUserId(userDetails);
        CartDto updatedCart = cartService.removeCoupon(userId);
        return ResponseEntity.ok(updatedCart);
    }
    
    // Helper method to extract user ID from UserDetails
    private Long extractUserId(UserDetails userDetails) {
        // TODO: Implement actual user ID extraction based on your UserDetails implementation
        // This is a placeholder - you would normally extract the user ID from your custom UserDetails implementation
        return 1L; // Placeholder
    }
}