package com.easykisan.repository;

import com.easykisan.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    Optional<Product> findBySlug(String slug);
    
    Optional<Product> findBySku(String sku);
    
    List<Product> findByNameContainingIgnoreCase(String name);
    
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
    @Query("SELECT p FROM Product p JOIN p.categories c WHERE c.id = :categoryId")
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
    
    @Query("SELECT p FROM Product p JOIN p.categories c WHERE c.slug = :categorySlug")
    Page<Product> findByCategorySlug(String categorySlug, Pageable pageable);
    
    Page<Product> findByVendorId(Long vendorId, Pageable pageable);
    
    Page<Product> findByVendorSlug(String vendorSlug, Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice")
    Page<Product> findByPriceRange(double minPrice, double maxPrice, Pageable pageable);
    
    @Query("SELECT p FROM Product p JOIN p.tags t WHERE t = :tag")
    Page<Product> findByTag(String tag, Pageable pageable);
    
    @Query(value = "SELECT * FROM products p ORDER BY p.created_at DESC LIMIT :limit", nativeQuery = true)
    List<Product> findLatestProducts(int limit);
    
    @Query(value = "SELECT * FROM products p ORDER BY p.rating DESC LIMIT :limit", nativeQuery = true)
    List<Product> findTopRatedProducts(int limit);
}