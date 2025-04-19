package com.easykisan.repository;

import com.easykisan.model.Refund;
import com.easykisan.model.Refund.RefundStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RefundRepository extends JpaRepository<Refund, Long> {
    
    List<Refund> findByOrderId(Long orderId);
    
    List<Refund> findByUserId(Long userId);
    
    Page<Refund> findByUserId(Long userId, Pageable pageable);
    
    Optional<Refund> findByRefundNumber(String refundNumber);
    
    List<Refund> findByStatus(RefundStatus status);
    
    Page<Refund> findByStatus(RefundStatus status, Pageable pageable);
    
    @Query("SELECT r FROM Refund r WHERE r.createdAt BETWEEN :startDate AND :endDate")
    List<Refund> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT r FROM Refund r WHERE r.createdAt BETWEEN :startDate AND :endDate")
    Page<Refund> findByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    @Query("SELECT SUM(r.amount) FROM Refund r WHERE r.status = 'COMPLETED'")
    Double getTotalRefundsAmount();
    
    @Query("SELECT SUM(r.amount) FROM Refund r WHERE r.status = 'COMPLETED' AND r.createdAt BETWEEN :startDate AND :endDate")
    Double getTotalRefundsAmountForPeriod(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT r.reason, COUNT(r) FROM Refund r GROUP BY r.reason")
    List<Object[]> getRefundReasonDistribution();
}