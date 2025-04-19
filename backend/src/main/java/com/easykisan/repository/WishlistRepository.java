package com.easykisan.repository;

import com.easykisan.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    
    Optional<Wishlist> findByUserId(Long userId);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM Wishlist w WHERE w.user.id = :userId")
    void deleteByUserId(Long userId);
}