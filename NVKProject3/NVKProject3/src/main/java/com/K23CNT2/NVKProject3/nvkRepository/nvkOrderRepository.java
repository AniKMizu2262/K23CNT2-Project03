package com.K23CNT2.NVKProject3.nvkRepository;

import com.K23CNT2.NVKProject3.nvkEntity.nvkCustomer;
import com.K23CNT2.NVKProject3.nvkEntity.nvkOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface nvkOrderRepository extends JpaRepository<nvkOrder, Long> {
    // Tìm đơn hàng của khách, sắp xếp mới nhất lên đầu
    List<nvkOrder> findByNvkCustomerOrderByNvkCreatedDateDesc(nvkCustomer nvkCustomer);
}