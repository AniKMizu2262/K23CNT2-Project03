package com.K23CNT2.NVKProject3.nvkController;

import com.K23CNT2.NVKProject3.nvkEntity.nvkAdmin;
import com.K23CNT2.NVKProject3.nvkRepository.nvkAdminRepository;
import com.K23CNT2.NVKProject3.nvkRepository.nvkCustomerRepository;
import com.K23CNT2.NVKProject3.nvkRepository.nvkOrderRepository;
import com.K23CNT2.NVKProject3.nvkService.nvkCategoryService;
import com.K23CNT2.NVKProject3.nvkService.nvkProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
@RequestMapping("/nvkAdmin")
public class nvkAdminController {

    // --- DEPENDENCIES ---
    @Autowired
    private nvkProductService productService;
    @Autowired
    private nvkCategoryService categoryService;
    @Autowired
    private nvkCustomerRepository customerRepo;
    @Autowired
    private nvkOrderRepository orderRepo;
    @Autowired
    private nvkAdminRepository adminRepository;

    // --- DASHBOARD ---
    @GetMapping("")
    public String adminDashboard(Model model) {
        // Thống kê số lượng
        model.addAttribute("prodCount", productService.getAllProducts().size());
        model.addAttribute("cateCount", categoryService.getAllCategories().size());
        model.addAttribute("userCount", customerRepo.count());
        model.addAttribute("orderCount", orderRepo.count());

        return "admin/index";
    }

    // --- PROFILE: XEM ---
    @GetMapping("/profile")
    public String viewProfile(Model model, HttpSession session) {
        // Lấy thông tin từ session
        nvkAdmin sessionAdmin = (nvkAdmin) session.getAttribute("nvkAdminLogin");
        // Lấy dữ liệu mới nhất từ DB
        nvkAdmin currentAdmin = adminRepository.findById(sessionAdmin.getNvkId()).orElse(null);

        model.addAttribute("nvkAdmin", currentAdmin);
        return "admin/profile";
    }

    // --- PROFILE: CẬP NHẬT ---
    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute("nvkAdmin") nvkAdmin nvkAdmin,
                                @RequestParam("nvkImageFile") MultipartFile file,
                                HttpSession session) {
        // 1. Xử lý ảnh đại diện
        if (!file.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path uploadPath = Paths.get("src/main/resources/static/images/");

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                try (InputStream inputStream = file.getInputStream()) {
                    Files.copy(inputStream, uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                }

                nvkAdmin.setNvkAvatar("/images/" + fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 2. Lưu vào DB
        adminRepository.save(nvkAdmin);

        // 3. Cập nhật lại Session
        session.setAttribute("nvkAdminLogin", nvkAdmin);

        return "redirect:/nvkAdmin/profile";
    }
}