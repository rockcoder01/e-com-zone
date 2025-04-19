package com.easybusy.api.services;

import com.easybusy.api.dto.ReviewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewService {
    
    ReviewDto createReview(Long userId, Long productId, ReviewDto reviewDto);
    
    ReviewDto getReviewById(Long id);
    
    List<ReviewDto> getReviewsByProductId(Long productId);
    
    Page<ReviewDto> getReviewsByProductId(Long productId, Pageable pageable);
    
    List<ReviewDto> getReviewsByUserId(Long userId);
    
    Page<ReviewDto> getReviewsByUserId(Long userId, Pageable pageable);
    
    ReviewDto updateReview(Long id, ReviewDto reviewDto);
    
    void deleteReview(Long id);
    
    ReviewDto markReviewAsHelpful(Long id);
    
    Double getAverageRatingByProductId(Long productId);
    
    Long getReviewCountByProductId(Long productId);
}