package com.easykisan.repository;

import com.easykisan.model.Address;
import com.easykisan.model.Address.AddressType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    
    List<Address> findByUserId(Long userId);
    
    List<Address> findByUserIdAndType(Long userId, AddressType type);
    
    Optional<Address> findByUserIdAndIsDefaultAndType(Long userId, boolean isDefault, AddressType type);
    
    @Modifying
    @Transactional
    @Query("UPDATE Address a SET a.isDefault = false WHERE a.user.id = :userId AND a.type = :type AND a.id <> :addressId")
    void resetDefaultAddress(Long userId, AddressType type, Long addressId);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM Address a WHERE a.user.id = :userId")
    void deleteByUserId(Long userId);
}