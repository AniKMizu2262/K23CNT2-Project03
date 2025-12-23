package com.K23CNT2.NVKProject3.nvkRepository;

import com.K23CNT2.NVKProject3.nvkEntity.nvkCustomer;
import com.K23CNT2.NVKProject3.nvkEntity.nvkOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface nvkOrderRepository extends JpaRepository<nvkOrder, Long> {

    // Tìm đơn hàng của khách
    List<nvkOrder> findByNvkCustomerOrderByNvkCreatedDateDesc(nvkCustomer nvkCustomer);

    // Tìm 5 đơn mới nhất
    List<nvkOrder> findTop5ByOrderByNvkCreatedDateDesc();

    // 🔴 QUAN TRỌNG: Hàm tính tổng tiền (Chỉ tính trạng thái 1, 2, 3)
    @Query("SELECT SUM(o.nvkTotalAmount) FROM nvkOrder o WHERE o.nvkStatus IN (1, 2, 3)")
    Double sumValidRevenue();

    @Query("SELECT o FROM nvkOrder o WHERE " +
            "(:status IS NULL OR o.nvkStatus = :status) AND " +
            "(:keyword IS NULL OR o.nvkCode LIKE %:keyword% OR o.nvkReceiverName LIKE %:keyword% OR o.nvkReceiverPhone LIKE %:keyword%) " +
            "ORDER BY o.nvkCreatedDate DESC")
    List<nvkOrder> findByStatusAndKeyword(@Param("status") Integer status, @Param("keyword") String keyword);
}