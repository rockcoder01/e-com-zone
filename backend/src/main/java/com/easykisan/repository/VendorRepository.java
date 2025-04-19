package com.easykisan.repository;

import com.easykisan.model.Vendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {
    
    Optional<Vendor> findBySlug(String slug);
    
    Optional<Vendor> findByUserId(Long userId);
    
    Optional<Vendor> findByEmail(String email);
    
    Boolean existsBySlug(String slug);
    
    Boolean existsByEmail(String email);
    
    Page<Vendor> findByIsActive(boolean isActive, Pageable pageable);
    
    Page<Vendor> findByIsActiveAndIsVerified(boolean isActive, boolean isVerified, Pageable pageable);
    
    Page<Vendor> findByIsActiveAndIsFeatured(boolean isActive, boolean isFeatured, Pageable pageable);
    
    @Query("SELECT v FROM Vendor v WHERE v.name LIKE %:keyword% OR v.description LIKE %:keyword% AND v.isActive = true")
    Page<Vendor> searchVendors(String keyword, Pageable pageable);
    
    @Query(value = "SELECT * FROM vendors v WHERE v.is_active = true ORDER BY v.rating DESC LIMIT :limit", nativeQuery = true)
    List<Vendor> findTopRatedVendors(int limit);
    
    @Query(value = "SELECT v.* FROM vendors v JOIN products p ON v.id = p.vendor_id GROUP BY v.id ORDER BY COUNT(p.id) DESC LIMIT :limit", nativeQuery = true)
    List<Vendor> findVendorsWithMostProducts(int limit);
}