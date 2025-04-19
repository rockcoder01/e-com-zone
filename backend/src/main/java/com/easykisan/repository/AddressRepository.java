package com.easykisan.repository;

import com.easykisan.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    
    List<Address> findByUserId(Long userId);
    
    List<Address> findByUserIdAndAddressType(Long userId, Address.AddressType addressType);
    
    Optional<Address> findByUserIdAndIsDefault(Long userId, boolean isDefault);
    
    Optional<Address> findByUserIdAndAddressTypeAndIsDefault(Long userId, Address.AddressType addressType, boolean isDefault);
    
    @Query("SELECT a FROM Address a WHERE a.user.id = :userId AND a.id = :addressId")
    Optional<Address> findByUserIdAndAddressId(Long userId, Long addressId);
    
    void deleteByUserId(Long userId);
}