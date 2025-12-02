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

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
@RequestMapping("/nvkAdmin")
public class nvkAdminController {

    // ========== SERVICE & REPOSITORY ==========
    @Autowired private nvkProductService productService;
    @Autowired private nvkCategoryService categoryService;
    @Autowired private nvkCustomerRepository customerRepo;
    @Autowired private nvkOrderRepository orderRepo;
    @Autowired private nvkAdminRepository adminRepository;

    // ========== DASHBOARD ==========
    @GetMapping("")
    public String adminDashboard(Model model) {

        model.addAttribute("prodCount", productService.getAllProducts().size());
        model.addAttribute("cateCount", categoryService.getAllCategories().size());

        // Thống kê User & Order
        model.addAttribute("userCount", customerRepo.count());
        model.addAttribute("orderCount", orderRepo.count());

        return "admin/index";
    }

    // ========== PROFILE: HIỂN THỊ ==========
    @GetMapping("/profile")
    public String viewProfile(Model model, HttpSession session) {

        // Lấy admin đang đăng nhập từ session
        nvkAdmin admin = (nvkAdmin) session.getAttribute("nvkAdminLogin");

        // Lấy bản mới nhất từ DB
        nvkAdmin currentAdmin = adminRepository.findById(admin.getNvkId()).orElse(null);

        model.addAttribute("nvkAdmin", currentAdmin);
        return "admin/profile";
    }

    // ========== PROFILE: CẬP NHẬT ==========
    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute("nvkAdmin") nvkAdmin nvkAdmin,
                                @RequestParam("nvkImageFile") MultipartFile file,
                                HttpSession session) {

        // --- Xử lý avatar upload ---
        if (!file.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path uploadPath = Paths.get("src/main/resources/static/images/");

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                try (InputStream inputStream = file.getInputStream()) {
                    Files.copy(inputStream, uploadPath.resolve(fileName),
                            StandardCopyOption.REPLACE_EXISTING);
                }

                nvkAdmin.setNvkAvatar("/images/" + fileName);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Lưu DB
        adminRepository.save(nvkAdmin);

        // Cập nhật session ngay để UI đổi avatar lập tức
        session.setAttribute("nvkAdminLogin", nvkAdmin);

        return "redirect:/nvkAdmin/profile";
    }
}
