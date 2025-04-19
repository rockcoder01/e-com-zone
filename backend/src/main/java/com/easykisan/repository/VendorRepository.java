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
    
    List<Vendor> findByNameContainingIgnoreCase(String name);
    
    Page<Vendor> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
    @Query("SELECT v FROM Vendor v WHERE v.isActive = true AND v.isVerified = true ORDER BY v.rating DESC")
    Page<Vendor> findTopVendors(Pageable pageable);
    
    @Query("SELECT v FROM Vendor v WHERE v.isActive = true AND v.isVerified = true ORDER BY v.createdAt DESC")
    Page<Vendor> findNewVendors(Pageable pageable);
    
    @Query(value = "SELECT * FROM vendors v ORDER BY v.rating DESC LIMIT :limit", nativeQuery = true)
    List<Vendor> findTopRatedVendors(int limit);
}