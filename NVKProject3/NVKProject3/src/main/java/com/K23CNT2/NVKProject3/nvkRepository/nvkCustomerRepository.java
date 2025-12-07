package com.K23CNT2.NVKProject3.nvkRepository;

import com.K23CNT2.NVKProject3.nvkEntity.nvkCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface nvkCustomerRepository extends JpaRepository<nvkCustomer, Long> {

    // --- Custom Queries ---
    Optional<nvkCustomer> findByNvkEmail(String nvkEmail);
}