package com.K23CNT2.NVKProject3.nvkController;

import com.K23CNT2.NVKProject3.nvkEntity.nvkOrder;
import com.K23CNT2.NVKProject3.nvkService.nvkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/nvkAdmin/order")
public class nvkOrderController {

    @Autowired
    private nvkOrderService orderService; // Dùng Service thay vì Repository

    // ==========================================================
    // 1. HIỂN THỊ DANH SÁCH (Kết hợp Tìm kiếm + Lọc)
    // ==========================================================
    @GetMapping("")
    public String listOrder(Model model,
                            @RequestParam(value = "status", required = false) Integer status,
                            @RequestParam(value = "keyword", required = false) String keyword) {

        // Gọi Service tìm kiếm và lọc
        List<nvkOrder> list = orderService.searchAndFilter(status, keyword);
        model.addAttribute("nvkOrders", list);

        // Giữ lại giá trị để hiển thị trên ô input/dropdown
        model.addAttribute("currStatus", status);
        model.addAttribute("currKeyword", keyword);

        return "admin/order/list";
    }

    // ==========================================================
    // 2. API LIVE SEARCH (Trả về Fragment HTML cho bảng)
    // ==========================================================
    @GetMapping("/search-results")
    public String searchResults(Model model,
                                @RequestParam(value = "status", required = false) Integer status,
                                @RequestParam(value = "keyword", required = false) String keyword) {

        // Gọi hàm searchAndFilter vừa viết bên Service
        List<nvkOrder> list = orderService.searchAndFilter(status, keyword);
        model.addAttribute("nvkOrders", list);

        // Trả về Fragment (chỉ cái bảng)
        return "admin/order/list :: order_rows";
    }

    // ==========================================================
    // 3. XEM CHI TIẾT ĐƠN HÀNG
    // ==========================================================
    @GetMapping("/view/{id}")
    public String viewOrder(@PathVariable("id") Long id, Model model) {
        nvkOrder order = orderService.getOrderById(id);
        if (order == null) {
            return "redirect:/nvkAdmin/order";
        }
        model.addAttribute("nvkOrder", order);
        return "admin/order/detail"; // Bro nhớ tạo file detail.html nhé
    }

    // ==========================================================
    // 4. CẬP NHẬT TRẠNG THÁI ĐƠN HÀNG
    // ==========================================================
    @PostMapping("/update-status")
    public String updateOrderStatus(@RequestParam("id") Long id,
                                    @RequestParam("status") Integer status,
                                    RedirectAttributes redirectAttributes) { // <--- 1. Thêm cái này

        nvkOrder order = orderService.getOrderById(id);
        if (order != null) {
            order.setNvkStatus(status);
            orderService.saveOrder(order);

            // 2. Gửi thông báo xanh
            redirectAttributes.addFlashAttribute("nvkMsg", "Đã cập nhật trạng thái đơn hàng thành công!");
        }

        // 3. Chuyển hướng về trang Danh sách (cho đồng bộ với Product/Category)
        return "redirect:/nvkAdmin/order";
    }
}