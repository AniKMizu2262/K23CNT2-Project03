package com.K23CNT2.NVKProject3.nvkController;

import com.K23CNT2.NVKProject3.nvkEntity.nvkCustomer;
import com.K23CNT2.NVKProject3.nvkService.nvkCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
@RequestMapping("/nvkAdmin/customer")
public class nvkCustomerController {

    @Autowired
    private nvkCustomerService customerService; // Sử dụng Service

    // ==========================================================
    // 1. HIỂN THỊ DANH SÁCH + TÌM KIẾM
    // ==========================================================
    @GetMapping("")
    public String listCustomer(Model model,
                               @RequestParam(value = "keyword", required = false) String keyword) {

        List<nvkCustomer> list = customerService.searchCustomer(keyword);
        model.addAttribute("nvkCustomers", list);
        model.addAttribute("keyword", keyword);

        return "admin/customer/list";
    }

    // ==========================================================
    // 2. API LIVE SEARCH (Trả về Fragment bảng)
    // ==========================================================
    @GetMapping("/search-results")
    public String searchResults(Model model,
                                @RequestParam(value = "keyword", required = false) String keyword) {

        List<nvkCustomer> list = customerService.searchCustomer(keyword);
        model.addAttribute("nvkCustomers", list);

        return "admin/customer/list :: customer_rows";
    }

    // ==========================================================
    // 3. FORM THÊM MỚI
    // ==========================================================
    @GetMapping("/create")
    public String createCustomer(Model model) {
        model.addAttribute("nvkCustomer", new nvkCustomer());
        return "admin/customer/form";
    }

    // ==========================================================
    // 4. FORM SỬA
    // ==========================================================
    @GetMapping("/edit/{id}")
    public String editCustomer(@PathVariable("id") Long id, Model model) {
        nvkCustomer customer = customerService.getCustomerById(id);
        if (customer == null) {
            return "redirect:/nvkAdmin/customer";
        }

        model.addAttribute("nvkCustomer", customer);
        return "admin/customer/form";
    }

    // ==========================================================
    // 5. LƯU KHÁCH HÀNG (CÓ UPLOAD AVATAR)
    // ==========================================================
    @PostMapping("/save")
    public String saveCustomer(@ModelAttribute("nvkCustomer") nvkCustomer nvkCustomer,
                               @RequestParam(value = "nvkImageFile", required = false) MultipartFile file,
                               RedirectAttributes redirectAttributes) {
        try {
            if (file != null && !file.isEmpty()) {
                // DEBUG: In ra tên file để kiểm tra
                System.out.println(">>> Đang upload file: " + file.getOriginalFilename());

                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path uploadPath = Paths.get("./uploads");

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                try (InputStream inputStream = file.getInputStream()) {
                    Files.copy(inputStream, uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                    System.out.println(">>> Lưu file thành công tại: " + uploadPath.resolve(fileName));
                }

                nvkCustomer.setNvkAvatar("/images/" + fileName);
            } else {
                // DEBUG
                System.out.println(">>> Không có file nào được chọn, giữ nguyên ảnh cũ.");
            }

            customerService.saveCustomer(nvkCustomer);
            redirectAttributes.addFlashAttribute("nvkMsg", "Cập nhật thành công!");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/nvkAdmin/customer";
    }

    // ==========================================================
    // 6. XÓA KHÁCH HÀNG
    // ==========================================================
    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable("id") Long id,
                                 RedirectAttributes redirectAttributes) {

        customerService.deleteCustomer(id);
        redirectAttributes.addFlashAttribute("nvkMsg", "Đã xóa khách hàng thành công!");

        return "redirect:/nvkAdmin/customer";
    }
}