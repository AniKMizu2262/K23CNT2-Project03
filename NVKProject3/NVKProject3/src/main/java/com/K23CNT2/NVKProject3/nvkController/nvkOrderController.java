package com.K23CNT2.NVKProject3.nvkController;

import com.K23CNT2.NVKProject3.nvkEntity.nvkOrder;
import com.K23CNT2.NVKProject3.nvkEntity.nvkOrderDetail;
import com.K23CNT2.NVKProject3.nvkRepository.nvkOrderDetailRepository;
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
    private nvkOrderService orderService;
    @Autowired
    private nvkOrderDetailRepository orderDetailRepo;

    // 1. Danh sách (Có tìm kiếm + Lọc)
    @GetMapping("")
    public String listOrder(Model model,
                            @RequestParam(value = "status", required = false) Integer status,
                            @RequestParam(value = "keyword", required = false) String keyword) {
        model.addAttribute("nvkOrders", orderService.searchAndFilter(status, keyword));
        model.addAttribute("currStatus", status);
        model.addAttribute("currKeyword", keyword);
        return "admin/order/list";
    }

    // 2. Live Search (Dành cho AJAX nếu có)
    @GetMapping("/search-results")
    public String searchResults(Model model,
                                @RequestParam(value = "status", required = false) Integer status,
                                @RequestParam(value = "keyword", required = false) String keyword) {
        model.addAttribute("nvkOrders", orderService.searchAndFilter(status, keyword));
        return "admin/order/list :: order_rows";
    }

    // 3. XEM CHI TIẾT
    @GetMapping("/detail/{id}")
    public String viewOrder(@PathVariable("id") Long id, Model model) {
        nvkOrder order = orderService.getOrderById(id);
        if (order == null) {
            return "redirect:/nvkAdmin/order";
        }

        // Lấy danh sách sản phẩm trong đơn
        List<nvkOrderDetail> details = orderDetailRepo.findByNvkOrder(order);

        // 👇 SỬA LẠI 2 DÒNG NÀY (Thêm chữ "nvk" vào trước) 👇
        model.addAttribute("nvkOrder", order);           // HTML đang chờ biến tên 'nvkOrder'
        model.addAttribute("nvkOrderDetails", details);  // HTML khả năng cũng chờ 'nvkOrderDetails'

        return "admin/order/detail";
    }

    // 4. Cập nhật trạng thái
    @PostMapping("/update-status")
    public String updateStatus(@RequestParam("id") Long id,
                               @RequestParam("status") Integer status,
                               RedirectAttributes ra) {
        nvkOrder order = orderService.getOrderById(id);
        if (order != null) {
            order.setNvkStatus(status);
            orderService.saveOrder(order);
            ra.addFlashAttribute("nvkMsg", "Cập nhật trạng thái đơn hàng thành công!");
        }
        return "redirect:/nvkAdmin/order/detail/" + id; // Redirect về trang chi tiết
    }
}