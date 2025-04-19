package com.easykisan.repository;

import com.easykisan.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    
    List<OrderItem> findByOrderId(Long orderId);
    
    @Query("SELECT oi.product.id AS productId, SUM(oi.quantity) AS totalQuantity " +
           "FROM OrderItem oi GROUP BY oi.product.id ORDER BY totalQuantity DESC LIMIT :limit")
    List<Map<String, Object>> findBestSellingProducts(int limit);
    
    @Query("SELECT oi.product.id AS productId, SUM(oi.quantity) AS totalQuantity " +
           "FROM OrderItem oi " +
           "WHERE oi.order.createdAt BETWEEN :startDate AND :endDate " +
           "GROUP BY oi.product.id ORDER BY totalQuantity DESC LIMIT :limit")
    List<Map<String, Object>> findBestSellingProductsForPeriod(LocalDateTime startDate, LocalDateTime endDate, int limit);
    
    @Query("SELECT oi.product.id AS productId, SUM(oi.quantity * oi.price) AS totalRevenue " +
           "FROM OrderItem oi " +
           "WHERE oi.order.status = 'COMPLETED' " +
           "GROUP BY oi.product.id ORDER BY totalRevenue DESC LIMIT :limit")
    List<Map<String, Object>> findTopRevenueGeneratingProducts(int limit);
    
    @Query("SELECT oi.product.category.id AS categoryId, SUM(oi.quantity) AS totalQuantity " +
           "FROM OrderItem oi " +
           "GROUP BY oi.product.category.id ORDER BY totalQuantity DESC LIMIT :limit")
    List<Map<String, Object>> findBestSellingCategories(int limit);
}