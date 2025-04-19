package com.easybusy.api.services.impl;

import com.easybusy.api.dto.CartDto;
import com.easybusy.api.dto.CartItemDto;
import com.easybusy.api.exceptions.ResourceNotFoundException;
import com.easybusy.api.models.Cart;
import com.easybusy.api.models.CartItem;
import com.easybusy.api.models.Product;
import com.easybusy.api.models.User;
import com.easybusy.api.repositories.CartRepository;
import com.easybusy.api.repositories.ProductRepository;
import com.easybusy.api.repositories.UserRepository;
import com.easybusy.api.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public CartDto getCartByUserId(Long userId) {
        Cart cart = getOrCreateCart(userId);
        return convertToDto(cart);
    }

    @Override
    @Transactional
    public CartDto addItemToCart(Long userId, CartItemDto cartItemDto) {
        Cart cart = getOrCreateCart(userId);
        
        Product product = productRepository.findById(cartItemDto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + cartItemDto.getProductId()));
        
        // Check if product already exists in cart
        Optional<CartItem> existingItemOpt = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();
        
        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + cartItemDto.getQuantity());
            existingItem.setPrice(product.getPrice());
            if (cartItemDto.getSelectedAttributes() != null) {
                existingItem.setSelectedAttributes(cartItemDto.getSelectedAttributes());
            }
        } else {
            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setCart(cart);
            newItem.setQuantity(cartItemDto.getQuantity());
            newItem.setPrice(product.getPrice());
            newItem.setSelectedAttributes(cartItemDto.getSelectedAttributes());
            cart.getItems().add(newItem);
        }
        
        cart.recalculateTotal();
        Cart savedCart = cartRepository.save(cart);
        
        return convertToDto(savedCart);
    }

    @Override
    @Transactional
    public CartDto updateCartItem(Long userId, Long cartItemId, CartItemDto cartItemDto) {
        Cart cart = getCartByUser(userId);
        
        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + cartItemId));
        
        cartItem.setQuantity(cartItemDto.getQuantity());
        if (cartItemDto.getSelectedAttributes() != null) {
            cartItem.setSelectedAttributes(cartItemDto.getSelectedAttributes());
        }
        
        cart.recalculateTotal();
        Cart savedCart = cartRepository.save(cart);
        
        return convertToDto(savedCart);
    }

    @Override
    @Transactional
    public CartDto removeItemFromCart(Long userId, Long cartItemId) {
        Cart cart = getCartByUser(userId);
        
        cart.getItems().removeIf(item -> item.getId().equals(cartItemId));
        cart.recalculateTotal();
        
        Cart savedCart = cartRepository.save(cart);
        return convertToDto(savedCart);
    }

    @Override
    @Transactional
    public CartDto clearCart(Long userId) {
        Cart cart = getCartByUser(userId);
        cart.getItems().clear();
        cart.setTotal(BigDecimal.ZERO);
        cart.setCouponCode(null);
        cart.setDiscount(BigDecimal.ZERO);
        
        Cart savedCart = cartRepository.save(cart);
        return convertToDto(savedCart);
    }

    @Override
    @Transactional
    public CartDto applyCoupon(Long userId, String couponCode) {
        Cart cart = getCartByUser(userId);
        
        // TODO: Implement coupon logic here
        cart.setCouponCode(couponCode);
        
        // For now, just set a dummy discount of 10%
        BigDecimal discountAmount = cart.getTotal().multiply(new BigDecimal("0.1"));
        cart.setDiscount(discountAmount);
        cart.setTotal(cart.getTotal().subtract(discountAmount));
        
        Cart savedCart = cartRepository.save(cart);
        return convertToDto(savedCart);
    }

    @Override
    @Transactional
    public CartDto removeCoupon(Long userId) {
        Cart cart = getCartByUser(userId);
        
        cart.setCouponCode(null);
        cart.setDiscount(BigDecimal.ZERO);
        cart.recalculateTotal();
        
        Cart savedCart = cartRepository.save(cart);
        return convertToDto(savedCart);
    }
    
    // Helper methods
    
    private Cart getOrCreateCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    newCart.setTotal(BigDecimal.ZERO);
                    return cartRepository.save(newCart);
                });
    }
    
    private Cart getCartByUser(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user with id: " + userId));
    }
    
    private CartDto convertToDto(Cart cart) {
        List<CartItemDto> itemDtos = cart.getItems().stream()
                .map(this::convertToItemDto)
                .collect(Collectors.toList());
        
        return CartDto.builder()
                .id(cart.getId())
                .userId(cart.getUser().getId())
                .items(itemDtos)
                .subtotal(cart.getTotal())
                .couponCode(cart.getCouponCode())
                .discount(cart.getDiscount())
                .total(cart.getTotal().subtract(cart.getDiscount()))
                .itemCount(cart.getItems().size())
                .build();
    }
    
    private CartItemDto convertToItemDto(CartItem item) {
        return CartItemDto.builder()
                .id(item.getId())
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .productImage(item.getProduct().getImages().isEmpty() ? null : item.getProduct().getImages().get(0))
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .total(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .selectedAttributes(item.getSelectedAttributes())
                .build();
    }
}