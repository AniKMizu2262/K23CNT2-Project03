package com.K23CNT2.NVKProject3.nvkController;

import com.K23CNT2.NVKProject3.nvkEntity.nvkCustomer;
import com.K23CNT2.NVKProject3.nvkService.nvkCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
@RequestMapping("/nvkAdmin/customer")
public class nvkCustomerController {

    @Autowired
    private nvkCustomerService customerService;

    @GetMapping("")
    public String listCustomer(Model model, @RequestParam(value = "keyword", required = false) String keyword) {
        model.addAttribute("nvkCustomers", customerService.searchCustomer(keyword));
        model.addAttribute("keyword", keyword);
        return "admin/customer/list";
    }

    @GetMapping("/search-results")
    public String searchResults(Model model, @RequestParam(value = "keyword", required = false) String keyword) {
        model.addAttribute("nvkCustomers", customerService.searchCustomer(keyword));
        return "admin/customer/list :: customer_rows";
    }

    @GetMapping("/create")
    public String createCustomer(Model model) {
        model.addAttribute("nvkCustomer", new nvkCustomer());
        return "admin/customer/form";
    }

    @GetMapping("/edit/{id}")
    public String editCustomer(@PathVariable("id") Long id, Model model) {
        nvkCustomer customer = customerService.getCustomerById(id);
        if (customer == null) return "redirect:/nvkAdmin/customer";

        model.addAttribute("nvkCustomer", customer);
        return "admin/customer/form";
    }

    @PostMapping("/save")
    public String saveCustomer(@ModelAttribute("nvkCustomer") nvkCustomer customer,
                               @RequestParam(value = "nvkImageFile", required = false) MultipartFile file,
                               RedirectAttributes ra) {
        try {
            if (file != null && !file.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path uploadPath = Paths.get("uploads", "user"); // Lưu vào uploads/user

                if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

                Files.copy(file.getInputStream(), uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                customer.setNvkAvatar("/nvk-images/" + fileName);
            }
            customerService.saveCustomer(customer);
            ra.addFlashAttribute("nvkMsg", "Cập nhật khách hàng thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("nvkMsg", "Lỗi: " + e.getMessage());
        }
        return "redirect:/nvkAdmin/customer";
    }

    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable("id") Long id, RedirectAttributes ra) {
        try {
            // Thử xóa cứng (nếu khách chưa mua gì thì xóa được)
            customerService.deleteCustomer(id);
            ra.addFlashAttribute("nvkMsg", "Đã xóa khách hàng thành công!");
        } catch (Exception e) {
            // Nếu lỗi (tức là khách đã có đơn hàng) -> Chuyển sang KHÓA tài khoản
            nvkCustomer customer = customerService.getCustomerById(id);
            if (customer != null) {
                customer.setNvkActive(false); // Chuyển trạng thái sang KHÓA (0)
                customerService.saveCustomer(customer);
                ra.addFlashAttribute("nvkMsg", "Khách hàng này đã có đơn hàng -> Hệ thống đã CHUYỂN SANG TRẠNG THÁI KHÓA!");
            } else {
                ra.addFlashAttribute("nvkMsg", "Lỗi: Không tìm thấy khách hàng!");
            }
        }
        return "redirect:/nvkAdmin/customer";
    }
}