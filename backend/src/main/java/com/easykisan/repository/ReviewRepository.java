package com.easykisan.repository;

import com.easykisan.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    List<Review> findByProductId(Long productId);
    
    Page<Review> findByProductId(Long productId, Pageable pageable);
    
    List<Review> findByUserId(Long userId);
    
    Page<Review> findByUserId(Long userId, Pageable pageable);
    
    Optional<Review> findByUserIdAndProductId(Long userId, Long productId);
    
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product.id = :productId")
    Double getAverageRatingForProduct(Long productId);
    
    @Query("SELECT COUNT(r) FROM Review r WHERE r.product.id = :productId")
    Long getReviewCountForProduct(Long productId);
    
    @Query("SELECT r.rating, COUNT(r) FROM Review r WHERE r.product.id = :productId GROUP BY r.rating ORDER BY r.rating DESC")
    List<Object[]> getRatingDistributionForProduct(Long productId);
    
    @Query("SELECT r FROM Review r WHERE r.product.id = :productId AND r.rating = :rating")
    Page<Review> findByProductIdAndRating(Long productId, Integer rating, Pageable pageable);
    
    @Query("SELECT r FROM Review r WHERE r.product.id = :productId AND r.isVerifiedPurchase = true")
    Page<Review> findVerifiedReviewsByProductId(Long productId, Pageable pageable);
}