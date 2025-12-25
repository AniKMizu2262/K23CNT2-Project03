package com.K23CNT2.NVKProject3.nvkRepository;

import com.K23CNT2.NVKProject3.nvkEntity.nvkOrder;
import com.K23CNT2.NVKProject3.nvkEntity.nvkOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface nvkOrderDetailRepository extends JpaRepository<nvkOrderDetail, Long> {

    /**
     * Lấy danh sách chi tiết sản phẩm của một đơn hàng
     *
     * @param nvkOrder Đơn hàng cần lấy chi tiết
     */
    List<nvkOrderDetail> findByNvkOrder(nvkOrder nvkOrder);
}