package com.easykisan.controller;

import com.easykisan.dto.CartDto;
import com.easykisan.dto.request.AddToCartRequest;
import com.easykisan.dto.request.UpdateCartItemRequest;
import com.easykisan.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CartDto> getCart(@RequestParam Long userId) {
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }
    
    @PostMapping("/add")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CartDto> addToCart(
            @RequestParam Long userId,
            @Valid @RequestBody AddToCartRequest addToCartRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cartService.addToCart(userId, addToCartRequest));
    }
    
    @PutMapping("/update/{productId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CartDto> updateCartItem(
            @RequestParam Long userId,
            @PathVariable Long productId,
            @Valid @RequestBody UpdateCartItemRequest updateCartItemRequest) {
        return ResponseEntity.ok(cartService.updateCartItem(userId, productId, updateCartItemRequest));
    }
    
    @DeleteMapping("/remove/{productId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CartDto> removeFromCart(
            @RequestParam Long userId,
            @PathVariable Long productId) {
        return ResponseEntity.ok(cartService.removeFromCart(userId, productId));
    }
    
    @DeleteMapping("/clear")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> clearCart(@RequestParam Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}