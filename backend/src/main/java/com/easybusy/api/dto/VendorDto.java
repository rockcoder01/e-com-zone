package com.easybusy.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorDto {
    private Long id;
    private String name;
    private String slug;
    private String logoUrl;
    private Double rating;
}