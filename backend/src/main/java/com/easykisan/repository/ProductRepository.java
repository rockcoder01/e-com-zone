package com.easykisan.repository;

import com.easykisan.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    Optional<Product> findBySlug(String slug);

    Boolean existsBySlug(String slug);
    
    Boolean existsBySku(String sku);
    
    Page<Product> findByIsActive(boolean isActive, Pageable pageable);
    
    Page<Product> findByIsActiveAndIsFeatured(boolean isActive, boolean isFeatured, Pageable pageable);
    
    Page<Product> findByIsActiveAndIsOnSale(boolean isActive, boolean isOnSale, Pageable pageable);
    
    @Query("SELECT p FROM Product p JOIN p.categories c WHERE c.id = :categoryId AND p.isActive = true")
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
    
    @Query("SELECT p FROM Product p JOIN p.vendor v WHERE v.id = :vendorId AND p.isActive = true")
    Page<Product> findByVendorId(Long vendorId, Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:keyword% OR p.description LIKE %:keyword% AND p.isActive = true")
    Page<Product> searchProducts(String keyword, Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice AND p.isActive = true")
    Page<Product> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
    
    @Query("SELECT p FROM Product p JOIN p.tags t WHERE t = :tag AND p.isActive = true")
    Page<Product> findByTag(String tag, Pageable pageable);
    
    @Query(value = "SELECT * FROM products p WHERE p.is_active = true ORDER BY p.created_at DESC LIMIT :limit", nativeQuery = true)
    List<Product> findLatestProducts(int limit);
    
    @Query(value = "SELECT * FROM products p WHERE p.is_active = true ORDER BY p.rating DESC LIMIT :limit", nativeQuery = true)
    List<Product> findTopRatedProducts(int limit);
    
    @Query(value = "SELECT * FROM products p JOIN product_tags pt ON p.id = pt.product_id WHERE pt.tag IN :tags AND p.is_active = true GROUP BY p.id ORDER BY COUNT(p.id) DESC LIMIT :limit", nativeQuery = true)
    List<Product> findRelatedProducts(List<String> tags, int limit);
}