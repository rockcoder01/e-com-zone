package com.easybusy.api.models;

import com.easybusy.api.models.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_payments")
public class OrderPayment extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private String transactionId;
    private BigDecimal amount;
    private String currency = "USD";

    @Column(columnDefinition = "TEXT")
    private String paymentDetails;

    private LocalDateTime paidAt;

    public enum PaymentMethod {
        CREDIT_CARD,
        PAYPAL,
        STRIPE,
        BANK_TRANSFER,
        CASH_ON_DELIVERY
    }

    public enum PaymentStatus {
        PENDING,
        COMPLETED,
        FAILED,
        REFUNDED,
        CANCELLED
    }
}