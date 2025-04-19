package com.easybusy.api.services;

import com.easybusy.api.dto.OrderDto;
import com.easybusy.api.dto.OrderRequest;
import com.easybusy.api.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    
    OrderDto createOrder(Long userId, OrderRequest orderRequest);
    
    OrderDto getOrderById(Long id);
    
    OrderDto getOrderByOrderNumber(String orderNumber);
    
    List<OrderDto> getOrdersByUserId(Long userId);
    
    Page<OrderDto> getOrdersByUserId(Long userId, Pageable pageable);
    
    OrderDto updateOrderStatus(Long id, Order.OrderStatus status);
    
    OrderDto cancelOrder(Long id);
    
    OrderDto processPayment(Long id, String paymentId);
    
    List<OrderDto> getOrdersByStatus(Order.OrderStatus status);
    
    Page<OrderDto> getOrdersByStatus(Order.OrderStatus status, Pageable pageable);
}