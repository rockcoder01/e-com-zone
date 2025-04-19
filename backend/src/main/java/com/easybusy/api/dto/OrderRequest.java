package com.easybusy.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private Long shippingAddressId;
    private Long billingAddressId;
    private String paymentMethod;
    private String notes;
    private String couponCode;
}