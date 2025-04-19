package com.easybusy.api.repositories;

import com.easybusy.api.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    List<Product> findByCategoriesId(Long categoryId);
    
    Page<Product> findByCategoriesId(Long categoryId, Pageable pageable);
    
    List<Product> findByNameContainingIgnoreCase(String name);
    
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE p.isActive = true")
    List<Product> findAllActive();
    
    @Query("SELECT p FROM Product p WHERE p.isActive = true")
    Page<Product> findAllActive(Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE p.stock > 0 AND p.isActive = true")
    List<Product> findAllInStock();
    
    @Query("SELECT p FROM Product p WHERE p.stock > 0 AND p.isActive = true")
    Page<Product> findAllInStock(Pageable pageable);
    
    @Query(value = "SELECT * FROM products p WHERE p.is_active = true ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<Product> findRandomProducts(int limit);
}