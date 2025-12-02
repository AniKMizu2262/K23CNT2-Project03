package com.K23CNT2.NVKProject3.nvkController;

import com.K23CNT2.NVKProject3.nvkEntity.nvkOrder;
import com.K23CNT2.NVKProject3.nvkRepository.nvkOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/nvkAdmin/order")
public class nvkOrderController {

    @Autowired
    private nvkOrderRepository orderRepository;

    // 1. Danh sách đơn hàng
    @GetMapping("")
    public String listOrder(Model model) {
        model.addAttribute("nvkOrders", orderRepository.findAll());
        return "admin/order/list";
    }

    // 2. Xem chi tiết đơn hàng
    @GetMapping("/view/{id}")
    public String viewOrder(@PathVariable("id") Long id, Model model) {
        nvkOrder order = orderRepository.findById(id).orElse(null);

        // Kiểm tra nếu ID không tồn tại thì quay về danh sách (tránh lỗi màn hình trắng)
        if (order == null) {
            return "redirect:/nvkAdmin/order";
        }

        model.addAttribute("nvkOrder", order);
        return "admin/order/detail";
    }

    // 3. Cập nhật trạng thái đơn hàng (Duyệt/Giao/Hủy)
    @PostMapping("/update-status")
    public String updateOrderStatus(@RequestParam("id") Long id,
                                    @RequestParam("status") Integer status) {
        nvkOrder order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            order.setNvkStatus(status);
            orderRepository.save(order);
        }
        return "redirect:/nvkAdmin/order/view/" + id; // Quay lại trang chi tiết
    }
}