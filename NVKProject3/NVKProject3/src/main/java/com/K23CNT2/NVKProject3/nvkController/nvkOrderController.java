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

    // --- LIST ---
    @GetMapping("")
    public String listOrder(Model model) {
        model.addAttribute("nvkOrders", orderRepository.findAll());
        return "admin/order/list";
    }

    // --- DETAIL ---
    @GetMapping("/view/{id}")
    public String viewOrder(@PathVariable("id") Long id, Model model) {
        nvkOrder order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            return "redirect:/nvkAdmin/order";
        }
        model.addAttribute("nvkOrder", order);
        return "admin/order/detail";
    }

    // --- UPDATE STATUS ---
    @PostMapping("/update-status")
    public String updateOrderStatus(@RequestParam("id") Long id,
                                    @RequestParam("status") Integer status) {
        nvkOrder order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            order.setNvkStatus(status);
            orderRepository.save(order);
        }
        return "redirect:/nvkAdmin/order/view/" + id;
    }
}