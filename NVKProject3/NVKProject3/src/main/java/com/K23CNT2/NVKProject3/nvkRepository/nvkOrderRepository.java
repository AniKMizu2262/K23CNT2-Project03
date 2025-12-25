package com.K23CNT2.NVKProject3.nvkRepository;

import com.K23CNT2.NVKProject3.nvkEntity.nvkCustomer;
import com.K23CNT2.NVKProject3.nvkEntity.nvkOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface nvkOrderRepository extends JpaRepository<nvkOrder, Long> {

    // Lịch sử đơn hàng của khách (Mới nhất lên đầu)
    List<nvkOrder> findByNvkCustomerOrderByNvkCreatedDateDesc(nvkCustomer nvkCustomer);

    // Lấy 5 đơn hàng mới nhất (Cho Dashboard)
    List<nvkOrder> findTop5ByOrderByNvkCreatedDateDesc();

    /**
     * Tính tổng doanh thu thực tế.
     * Chỉ tính các đơn có trạng thái: 1 (Đã xác nhận), 2 (Đang giao), 3 (Đã giao).
     * Bỏ qua đơn Hủy (4) hoặc Chờ (0).
     */
    @Query("SELECT SUM(o.nvkTotalAmount) FROM nvkOrder o WHERE o.nvkStatus IN (1, 2, 3)")
    Double sumValidRevenue();

    /**
     * Tìm kiếm và lọc đơn hàng (Live Search Admin).
     *
     * @param status  Trạng thái đơn hàng (null = tất cả)
     * @param keyword Mã đơn, Tên người nhận, SĐT
     */
    @Query("SELECT o FROM nvkOrder o WHERE " +
            "(:status IS NULL OR o.nvkStatus = :status) AND " +
            "(:keyword IS NULL OR o.nvkCode LIKE %:keyword% " +
            "OR o.nvkReceiverName LIKE %:keyword% " +
            "OR o.nvkReceiverPhone LIKE %:keyword%) " +
            "ORDER BY o.nvkCreatedDate DESC")
    List<nvkOrder> findByStatusAndKeyword(@Param("status") Integer status, @Param("keyword") String keyword);
}