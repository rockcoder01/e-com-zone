package com.easybusy.api.services;

import com.easybusy.api.dto.AddressDto;

import java.util.List;

public interface AddressService {
    
    AddressDto createAddress(Long userId, AddressDto addressDto);
    
    AddressDto getAddressById(Long id);
    
    List<AddressDto> getAddressesByUserId(Long userId);
    
    AddressDto updateAddress(Long id, AddressDto addressDto);
    
    void deleteAddress(Long id);
    
    AddressDto setDefaultAddress(Long userId, Long addressId);
    
    AddressDto getDefaultAddress(Long userId);
}