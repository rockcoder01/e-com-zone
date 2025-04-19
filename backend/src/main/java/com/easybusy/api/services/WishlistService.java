package com.easybusy.api.services;

import com.easybusy.api.dto.ProductDto;
import com.easybusy.api.dto.WishlistDto;

import java.util.List;

public interface WishlistService {
    
    WishlistDto getWishlistByUserId(Long userId);
    
    WishlistDto addProductToWishlist(Long userId, Long productId);
    
    WishlistDto removeProductFromWishlist(Long userId, Long productId);
    
    boolean isProductInWishlist(Long userId, Long productId);
    
    List<ProductDto> getWishlistProducts(Long userId);
    
    void clearWishlist(Long userId);
}