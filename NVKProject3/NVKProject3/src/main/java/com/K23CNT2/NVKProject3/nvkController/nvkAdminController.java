package com.K23CNT2.NVKProject3.nvkController;

import com.K23CNT2.NVKProject3.nvkEntity.nvkAdmin;
import com.K23CNT2.NVKProject3.nvkEntity.nvkOrder;
import com.K23CNT2.NVKProject3.nvkRepository.nvkAdminRepository;
import com.K23CNT2.NVKProject3.nvkRepository.nvkCustomerRepository;
import com.K23CNT2.NVKProject3.nvkRepository.nvkOrderRepository;
import com.K23CNT2.NVKProject3.nvkRepository.nvkReviewRepository;
import com.K23CNT2.NVKProject3.nvkService.nvkCategoryService;
import com.K23CNT2.NVKProject3.nvkService.nvkProductService;
import jakarta.servlet.http.HttpSession;
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
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/nvkAdmin")
public class nvkAdminController {

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
    @Autowired
    private nvkReviewRepository reviewRepo;

    // --- DASHBOARD ---
    @GetMapping({"", "/", "/dashboard"})
    public String adminDashboard(Model model) {
        model.addAttribute("prodCount", productService.getAllProducts().size());
        model.addAttribute("cateCount", categoryService.getAllCategories().size());
        model.addAttribute("userCount", customerRepo.count());
        model.addAttribute("orderCount", orderRepo.count());

        Double revenue = orderRepo.sumValidRevenue();
        model.addAttribute("revenue", (revenue == null) ? 0.0 : revenue);

        model.addAttribute("reviewCount", reviewRepo.count());
        Double avgRating = reviewRepo.averageRating();
        model.addAttribute("avgRating", (avgRating == null) ? 0.0 : avgRating);

        List<nvkOrder> recentOrders = orderRepo.findTop5ByOrderByNvkCreatedDateDesc();
        model.addAttribute("recentOrders", (recentOrders == null) ? new ArrayList<>() : recentOrders);

        return "admin/index";
    }

    // --- PROFILE ---
    @GetMapping("/profile")
    public String viewProfile(Model model, HttpSession session) {
        nvkAdmin sessionAdmin = (nvkAdmin) session.getAttribute("nvkAdminLogin");
        if (sessionAdmin == null) return "redirect:/nvkAdmin/login";

        nvkAdmin currentAdmin = adminRepository.findById(sessionAdmin.getNvkId()).orElse(null);
        model.addAttribute("nvkAdmin", currentAdmin);
        return "admin/profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute("nvkAdmin") nvkAdmin nvkAdminForm,
                                @RequestParam("nvkImageFile") MultipartFile file,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {

        nvkAdmin currentAdmin = adminRepository.findById(nvkAdminForm.getNvkId()).orElse(null);
        if (currentAdmin != null) {
            currentAdmin.setNvkFullName(nvkAdminForm.getNvkFullName());
            if (nvkAdminForm.getNvkPassword() != null && !nvkAdminForm.getNvkPassword().isEmpty()) {
                currentAdmin.setNvkPassword(nvkAdminForm.getNvkPassword());
            }
            saveAvatar(currentAdmin, file);
            adminRepository.save(currentAdmin);
            session.setAttribute("nvkAdminLogin", currentAdmin);
            redirectAttributes.addFlashAttribute("successMsg", "Cập nhật hồ sơ thành công! ✅");
        }
        return "redirect:/nvkAdmin/profile";
    }

    // --- ACCOUNTS (QUẢN LÝ ADMIN KHÁC) ---
    @GetMapping("/accounts")
    public String listAccounts(Model model) {
        model.addAttribute("nvkAdmins", adminRepository.findAll());
        return "admin/accounts/list";
    }

    @GetMapping("/accounts/create")
    public String createAccount(Model model) {
        model.addAttribute("nvkAdmin", new nvkAdmin());
        return "admin/accounts/form";
    }

    @GetMapping("/accounts/edit/{id}")
    public String editAccount(@PathVariable("id") Long id, Model model) {
        nvkAdmin admin = adminRepository.findById(id).orElse(null);
        if (admin == null) return "redirect:/nvkAdmin/accounts";

        model.addAttribute("nvkAdmin", admin);
        return "admin/accounts/form";
    }

    @PostMapping("/accounts/save")
    public String saveAccount(@ModelAttribute("nvkAdmin") nvkAdmin nvkAdminForm,
                              @RequestParam("nvkImageFile") MultipartFile file,
                              RedirectAttributes redirectAttributes) {

        nvkAdmin currentAdmin = (nvkAdminForm.getNvkId() != null)
                ? adminRepository.findById(nvkAdminForm.getNvkId()).orElse(new nvkAdmin())
                : new nvkAdmin();

        currentAdmin.setNvkUsername(nvkAdminForm.getNvkUsername());
        currentAdmin.setNvkFullName(nvkAdminForm.getNvkFullName());
        currentAdmin.setNvkActive(nvkAdminForm.getNvkActive());

        if (nvkAdminForm.getNvkPassword() != null && !nvkAdminForm.getNvkPassword().isEmpty()) {
            currentAdmin.setNvkPassword(nvkAdminForm.getNvkPassword());
        }

        saveAvatar(currentAdmin, file);
        adminRepository.save(currentAdmin);

        redirectAttributes.addFlashAttribute("successMsg", "Lưu tài khoản thành công! 🎉");
        return "redirect:/nvkAdmin/accounts";
    }

    @GetMapping("/accounts/delete/{id}")
    public String deleteAccount(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        adminRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMsg", "Đã xóa tài khoản thành công! 🗑️");
        return "redirect:/nvkAdmin/accounts";
    }

    // --- HELPER ---
    private void saveAvatar(nvkAdmin admin, MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path uploadPath = Paths.get("uploads/admin");
                if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
                Files.copy(file.getInputStream(), uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                admin.setNvkAvatar("/nvk-images/" + fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}