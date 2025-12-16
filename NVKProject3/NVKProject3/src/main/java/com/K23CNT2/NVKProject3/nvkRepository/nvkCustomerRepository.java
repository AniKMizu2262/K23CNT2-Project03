package com.K23CNT2.NVKProject3.nvkRepository;

import com.K23CNT2.NVKProject3.nvkEntity.nvkCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface nvkCustomerRepository extends JpaRepository<nvkCustomer, Long> {
    // Tìm khách hàng theo Email (Trả về Object hoặc Null)
    nvkCustomer findByNvkEmail(String nvkEmail);
}