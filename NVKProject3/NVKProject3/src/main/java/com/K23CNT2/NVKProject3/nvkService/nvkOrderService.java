package com.K23CNT2.NVKProject3.nvkService;

import com.K23CNT2.NVKProject3.nvkEntity.nvkOrder;
import com.K23CNT2.NVKProject3.nvkRepository.nvkOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class nvkOrderService {

    @Autowired
    private nvkOrderRepository orderRepository;

    // Lấy chi tiết đơn hàng
    public nvkOrder getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    // Lưu đơn hàng
    public void saveOrder(nvkOrder order) {
        orderRepository.save(order);
    }

    // --- [QUAN TRỌNG] HÀM GỌI LIVE SEARCH ---
    public List<nvkOrder> searchAndFilter(Integer status, String keyword) {
        // Xử lý keyword: nếu rỗng thì cho null để Repository biết là không tìm theo tên
        if (keyword != null && keyword.trim().isEmpty()) {
            keyword = null;
        }
        return orderRepository.findByStatusAndKeyword(status, keyword);
    }
}