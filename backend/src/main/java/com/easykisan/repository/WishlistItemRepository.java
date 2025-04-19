package com.easykisan.repository;

import com.easykisan.model.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {
    
    List<WishlistItem> findByWishlistId(Long wishlistId);
    
    Optional<WishlistItem> findByWishlistIdAndProductId(Long wishlistId, Long productId);
    
    @Query("SELECT CASE WHEN COUNT(wi) > 0 THEN true ELSE false END FROM WishlistItem wi " +
           "WHERE wi.wishlist.id = :wishlistId AND wi.product.id = :productId")
    boolean existsByWishlistIdAndProductId(Long wishlistId, Long productId);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM WishlistItem wi WHERE wi.wishlist.id = :wishlistId")
    void deleteByWishlistId(Long wishlistId);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM WishlistItem wi WHERE wi.wishlist.id = :wishlistId AND wi.product.id = :productId")
    void deleteByWishlistIdAndProductId(Long wishlistId, Long productId);
}