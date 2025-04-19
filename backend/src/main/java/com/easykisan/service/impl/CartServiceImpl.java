package com.easykisan.service.impl;

import com.easykisan.dto.request.CartRequest;
import com.easykisan.dto.response.CartResponse;
import com.easykisan.dto.response.CartResponse.CartItemResponse;
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
import com.easykisan.service.interfaces.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository, CartItemRepository cartItemRepository,
                           UserRepository userRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public CartResponse getCartByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Cart cart = user.getCart();
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cart = cartRepository.save(cart);
            user.setCart(cart);
            userRepository.save(user);
        }

        return convertToCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse addItemToCart(Long userId, CartRequest cartRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Product product = productRepository.findById(cartRequest.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", cartRequest.getProductId()));

        if (product.getStock() < cartRequest.getQuantity()) {
            throw new BadRequestException("Product out of stock. Available: " + product.getStock());
        }

        Cart cart = user.getCart();
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cart = cartRepository.save(cart);
            user.setCart(cart);
            userRepository.save(user);
        }

        // Check if product already exists in cart
        Optional<CartItem> existingItemOpt = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(cartRequest.getProductId()))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + cartRequest.getQuantity());
            existingItem.setSelectedAttributes(cartRequest.getSelectedAttributes());
            existingItem.calculateSubtotal();
            cartItemRepository.save(existingItem);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(cartRequest.getQuantity());
            newItem.setUnitPrice(product.getPrice());
            newItem.setSelectedAttributes(cartRequest.getSelectedAttributes());
            newItem.calculateSubtotal();
            cart.addItem(newItem);
            cartItemRepository.save(newItem);
        }

        cart.calculateTotals();
        cart = cartRepository.save(cart);

        return convertToCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse updateCartItem(Long userId, Long itemId, CartRequest cartRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Cart cart = user.getCart();
        if (cart == null) {
            throw new ResourceNotFoundException("Cart", "userId", userId);
        }

        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("CartItem", "id", itemId));

        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new BadRequestException("Cart item does not belong to this user");
        }

        Product product = productRepository.findById(cartRequest.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", cartRequest.getProductId()));

        if (product.getStock() < cartRequest.getQuantity()) {
            throw new BadRequestException("Product out of stock. Available: " + product.getStock());
        }

        // Update cart item
        cartItem.setProduct(product);
        cartItem.setQuantity(cartRequest.getQuantity());
        cartItem.setUnitPrice(product.getPrice());
        cartItem.setSelectedAttributes(cartRequest.getSelectedAttributes());
        cartItem.calculateSubtotal();
        cartItemRepository.save(cartItem);

        cart.calculateTotals();
        cart = cartRepository.save(cart);

        return convertToCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse removeItemFromCart(Long userId, Long itemId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Cart cart = user.getCart();
        if (cart == null) {
            throw new ResourceNotFoundException("Cart", "userId", userId);
        }

        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("CartItem", "id", itemId));

        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new BadRequestException("Cart item does not belong to this user");
        }

        cart.removeItem(cartItem);
        cartItemRepository.delete(cartItem);

        cart.calculateTotals();
        cart = cartRepository.save(cart);

        return convertToCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse clearCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Cart cart = user.getCart();
        if (cart == null) {
            throw new ResourceNotFoundException("Cart", "userId", userId);
        }

        cartItemRepository.deleteByCartId(cart.getId());
        cart.clearItems();
        cart.calculateTotals();
        cart = cartRepository.save(cart);

        return convertToCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse applyCoupon(Long userId, String couponCode) {
        // TODO: Implement coupon validation and application logic
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Cart cart = user.getCart();
        if (cart == null) {
            throw new ResourceNotFoundException("Cart", "userId", userId);
        }

        // Placeholder for coupon validation logic
        // This would typically check against a coupon database and apply rules
        
        // For now, just applying a simple 10% discount for any coupon code as an example
        cart.setCouponCode(couponCode);
        cart.setDiscount(cart.getSubtotal().multiply(new BigDecimal("0.1")));
        cart.calculateTotals();
        cart = cartRepository.save(cart);

        return convertToCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse removeCoupon(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Cart cart = user.getCart();
        if (cart == null) {
            throw new ResourceNotFoundException("Cart", "userId", userId);
        }

        cart.setCouponCode(null);
        cart.setDiscount(BigDecimal.ZERO);
        cart.calculateTotals();
        cart = cartRepository.save(cart);

        return convertToCartResponse(cart);
    }
    
    /**
     * Convert Cart entity to CartResponse DTO
     * @param cart Cart entity
     * @return CartResponse DTO
     */
    private CartResponse convertToCartResponse(Cart cart) {
        CartResponse response = new CartResponse();
        response.setId(cart.getId());
        response.setUserId(cart.getUser() != null ? cart.getUser().getId() : null);
        response.setSubtotal(cart.getSubtotal());
        response.setTax(cart.getTax());
        response.setShipping(cart.getShipping());
        response.setDiscount(cart.getDiscount());
        response.setTotal(cart.getTotal());
        response.setCouponCode(cart.getCouponCode());
        response.setStatus(cart.getStatus().name());
        response.setItemCount(cart.getItems().size());
        
        response.setItems(cart.getItems().stream().map(this::convertToCartItemResponse).collect(Collectors.toList()));
        
        return response;
    }
    
    /**
     * Convert CartItem entity to CartItemResponse DTO
     * @param item CartItem entity
     * @return CartItemResponse DTO
     */
    private CartItemResponse convertToCartItemResponse(CartItem item) {
        CartItemResponse response = new CartItemResponse();
        response.setId(item.getId());
        response.setProductId(item.getProduct().getId());
        response.setProductName(item.getProduct().getName());
        response.setProductSlug(item.getProduct().getSlug());
        response.setProductImage(!item.getProduct().getImages().isEmpty() ? item.getProduct().getImages().get(0) : null);
        response.setUnitPrice(item.getUnitPrice());
        response.setSubtotal(item.getSubtotal());
        response.setDiscount(item.getDiscount());
        response.setQuantity(item.getQuantity());
        response.setSelectedAttributes(item.getSelectedAttributes());
        response.setVendorName(item.getProduct().getVendor() != null ? item.getProduct().getVendor().getName() : null);
        
        return response;
    }
}