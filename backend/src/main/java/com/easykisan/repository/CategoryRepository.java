package com.easykisan.repository;

import com.easykisan.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    Optional<Category> findBySlug(String slug);
    
    Boolean existsBySlug(String slug);
    
    Boolean existsByName(String name);
    
    List<Category> findByParentIdIsNull();
    
    List<Category> findByParentId(Long parentId);
    
    List<Category> findByIsActive(boolean isActive);
    
    List<Category> findByIsActiveOrderByDisplayOrderAsc(boolean isActive);
    
    @Query("SELECT c FROM Category c WHERE c.parent IS NULL AND c.isActive = true ORDER BY c.displayOrder ASC")
    List<Category> findAllParentCategories();
    
    @Query("SELECT c FROM Category c JOIN FETCH c.children WHERE c.id = :categoryId")
    Optional<Category> findByIdWithChildren(Long categoryId);
    
    @Query("SELECT c FROM Category c JOIN FETCH c.products WHERE c.id = :categoryId")
    Optional<Category> findByIdWithProducts(Long categoryId);
    
    @Query("SELECT DISTINCT c FROM Category c JOIN c.products p WHERE p.id = :productId")
    List<Category> findByProductId(Long productId);
    
    @Query(value = "SELECT c.* FROM categories c JOIN product_categories pc ON c.id = pc.category_id GROUP BY c.id ORDER BY COUNT(pc.product_id) DESC LIMIT :limit", nativeQuery = true)
    List<Category> findMostPopularCategories(int limit);
}