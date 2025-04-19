package com.easykisan.controller;

import com.easykisan.dto.request.CartRequest;
import com.easykisan.dto.response.ApiResponse;
import com.easykisan.dto.response.CartResponse;
import com.easykisan.security.UserDetailsImpl;
import com.easykisan.service.interfaces.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<?> getCart(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        CartResponse cart = cartService.getCartByUserId(userDetails.getId());
        return ResponseEntity.ok(ApiResponse.success("Cart retrieved successfully", cart));
    }

    @PostMapping("/items")
    public ResponseEntity<?> addItemToCart(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody CartRequest cartRequest) {
        CartResponse updatedCart = cartService.addItemToCart(userDetails.getId(), cartRequest);
        return ResponseEntity.ok(ApiResponse.success("Item added to cart successfully", updatedCart));
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<?> updateCartItem(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long itemId,
            @Valid @RequestBody CartRequest cartRequest) {
        CartResponse updatedCart = cartService.updateCartItem(userDetails.getId(), itemId, cartRequest);
        return ResponseEntity.ok(ApiResponse.success("Cart item updated successfully", updatedCart));
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<?> removeItemFromCart(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long itemId) {
        CartResponse updatedCart = cartService.removeItemFromCart(userDetails.getId(), itemId);
        return ResponseEntity.ok(ApiResponse.success("Item removed from cart successfully", updatedCart));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        CartResponse emptyCart = cartService.clearCart(userDetails.getId());
        return ResponseEntity.ok(ApiResponse.success("Cart cleared successfully", emptyCart));
    }

    @PostMapping("/coupon/{couponCode}")
    public ResponseEntity<?> applyCoupon(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable String couponCode) {
        CartResponse updatedCart = cartService.applyCoupon(userDetails.getId(), couponCode);
        return ResponseEntity.ok(ApiResponse.success("Coupon applied successfully", updatedCart));
    }

    @DeleteMapping("/coupon")
    public ResponseEntity<?> removeCoupon(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        CartResponse updatedCart = cartService.removeCoupon(userDetails.getId());
        return ResponseEntity.ok(ApiResponse.success("Coupon removed successfully", updatedCart));
    }
}