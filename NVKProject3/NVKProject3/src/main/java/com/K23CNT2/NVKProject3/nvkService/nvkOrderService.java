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

    public nvkOrder getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public void saveOrder(nvkOrder order) {
        orderRepository.save(order);
    }

    /**
     * Tìm kiếm và lọc đơn hàng (Live Search)
     *
     * @param status  Trạng thái đơn hàng (0,1,2...)
     * @param keyword Tên khách/SĐT/Mã đơn
     */
    public List<nvkOrder> searchAndFilter(Integer status, String keyword) {
        if (keyword != null && keyword.trim().isEmpty()) {
            keyword = null;
        }
        return orderRepository.findByStatusAndKeyword(status, keyword);
    }
}