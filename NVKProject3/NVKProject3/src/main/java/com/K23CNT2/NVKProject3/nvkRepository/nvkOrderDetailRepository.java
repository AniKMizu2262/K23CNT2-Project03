package com.K23CNT2.NVKProject3.nvkRepository;

import com.K23CNT2.NVKProject3.nvkEntity.nvkOrder;
import com.K23CNT2.NVKProject3.nvkEntity.nvkOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface nvkOrderDetailRepository extends JpaRepository<nvkOrderDetail, Long> {
    // Lấy các sản phẩm trong 1 đơn hàng
    List<nvkOrderDetail> findByNvkOrder(nvkOrder nvkOrder);
}