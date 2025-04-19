package com.easykisan.service.impl;

import com.easykisan.dto.CartDto;
import com.easykisan.dto.CartItemDto;
import com.easykisan.dto.request.AddToCartRequest;
import com.easykisan.dto.request.UpdateCartItemRequest;
import com.easykisan.exception.BadRequestException;
import com.easykisan.exception.ResourceNotFoundException;
import com.easykisan.model.Cart;
import com.easykisan.model.CartItem;
import com.easykisan.model.Product;
import com.easykisan.model.User;
import com.easykisan.repository.CartItemRepository;
import com.easykisan.repository.CartRepository;
import com.easykisan.repository.ProductRepository;
import com.easykisan.repository.UserRepository;
import com.easykisan.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    
    private static final double TAX_RATE = 0.18; // 18% tax rate
    
    @Override
    public CartDto getCartByUserId(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user id: " + userId));
        
        return convertToDto(cart);
    }
    
    @Override
    @Transactional
    public CartDto addToCart(Long userId, AddToCartRequest addToCartRequest) {
        Cart cart = getOrCreateCartEntity(userId);
        
        Product product = productRepository.findById(addToCartRequest.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + addToCartRequest.getProductId()));
        
        if (!product.isActive()) {
            throw new BadRequestException("Product is not active and cannot be added to cart");
        }
        
        if (product.getStock() < addToCartRequest.getQuantity()) {
            throw new BadRequestException("Not enough stock available. Available: " + product.getStock());
        }
        
        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId())
                .orElse(new CartItem());
        
        if (cartItem.getId() == null) {
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(addToCartRequest.getQuantity());
            cartItem.setPrice(product.getPrice());
            cartItem.setAddedAt(LocalDateTime.now());
        } else {
            int newQuantity = cartItem.getQuantity() + addToCartRequest.getQuantity();
            
            if (product.getStock() < newQuantity) {
                throw new BadRequestException("Not enough stock available. Available: " + product.getStock());
            }
            
            cartItem.setQuantity(newQuantity);
            cartItem.setPrice(product.getPrice()); // Update price in case it changed
            cartItem.setUpdatedAt(LocalDateTime.now());
        }
        
        cartItemRepository.save(cartItem);
        updateCartTotals(cart);
        
        return convertToDto(cart);
    }
    
    @Override
    @Transactional
    public CartDto updateCartItem(Long userId, Long productId, UpdateCartItemRequest updateCartItemRequest) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user id: " + userId));
        
        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found for product id: " + productId));
        
        Product product = cartItem.getProduct();
        
        if (product.getStock() < updateCartItemRequest.getQuantity()) {
            throw new BadRequestException("Not enough stock available. Available: " + product.getStock());
        }
        
        cartItem.setQuantity(updateCartItemRequest.getQuantity());
        cartItem.setUpdatedAt(LocalDateTime.now());
        
        cartItemRepository.save(cartItem);
        updateCartTotals(cart);
        
        return convertToDto(cart);
    }
    
    @Override
    @Transactional
    public CartDto removeFromCart(Long userId, Long productId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user id: " + userId));
        
        cartItemRepository.deleteByCartIdAndProductId(cart.getId(), productId);
        updateCartTotals(cart);
        
        return convertToDto(cart);
    }
    
    @Override
    @Transactional
    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user id: " + userId));
        
        cartItemRepository.deleteByCartId(cart.getId());
        updateCartTotals(cart);
    }
    
    @Override
    public CartDto getOrCreateCart(Long userId) {
        Cart cart = getOrCreateCartEntity(userId);
        return convertToDto(cart);
    }
    
    private Cart getOrCreateCartEntity(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    newCart.setCreatedAt(LocalDateTime.now());
                    newCart.setItems(new ArrayList<>());
                    newCart.setSubtotal(0.0);
                    newCart.setTax(0.0);
                    newCart.setDiscount(0.0);
                    newCart.setTotal(0.0);
                    return cartRepository.save(newCart);
                });
    }
    
    private void updateCartTotals(Cart cart) {
        List<CartItem> items = cartItemRepository.findByCartId(cart.getId());
        
        double subtotal = 0.0;
        
        for (CartItem item : items) {
            subtotal += item.getPrice() * item.getQuantity();
        }
        
        double tax = subtotal * TAX_RATE;
        double total = subtotal + tax - cart.getDiscount();
        
        cart.setSubtotal(subtotal);
        cart.setTax(tax);
        cart.setTotal(total);
        cart.setUpdatedAt(LocalDateTime.now());
        
        cartRepository.save(cart);
    }
    
    private CartDto convertToDto(Cart cart) {
        List<CartItem> items = cartItemRepository.findByCartId(cart.getId());
        
        List<CartItemDto> cartItemDtos = items.stream()
                .map(item -> {
                    Product product = item.getProduct();
                    return CartItemDto.builder()
                            .id(item.getId())
                            .productId(product.getId())
                            .productName(product.getName())
                            .productImage(product.getImages() != null && !product.getImages().isEmpty() ? 
                                    product.getImages().get(0) : "")
                            .price(item.getPrice())
                            .quantity(item.getQuantity())
                            .subtotal(item.getPrice() * item.getQuantity())
                            .sku(product.getSku())
                            .inStock(product.getStock() > 0)
                            .maxQuantity(product.getStock())
                            .build();
                })
                .collect(Collectors.toList());
        
        return CartDto.builder()
                .id(cart.getId())
                .userId(cart.getUser().getId())
                .items(cartItemDtos)
                .subtotal(cart.getSubtotal())
                .tax(cart.getTax())
                .shippingCost(cart.getShippingCost())
                .discount(cart.getDiscount())
                .total(cart.getTotal())
                .itemCount(cartItemDtos.size())
                .createdAt(cart.getCreatedAt())
                .updatedAt(cart.getUpdatedAt())
                .build();
    }
}