package com.K23CNT2.NVKProject3.nvkController;

import com.K23CNT2.NVKProject3.nvkEntity.nvkAdmin;
import com.K23CNT2.NVKProject3.nvkEntity.nvkOrder;
import com.K23CNT2.NVKProject3.nvkEntity.nvkOrderDetail;
import com.K23CNT2.NVKProject3.nvkRepository.*;
import com.K23CNT2.NVKProject3.nvkService.nvkCategoryService;
import com.K23CNT2.NVKProject3.nvkService.nvkProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

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
    @Autowired
    private nvkReviewRepository reviewRepo;
    @Autowired
    private nvkOrderDetailRepository orderDetailRepo;

    // =============================================================
    // PHẦN 1: DASHBOARD & PROFILE
    // =============================================================

    // Dashboard
    @GetMapping({"", "/", "/dashboard"})
    public String adminDashboard(Model model) {
        // Thống kê
        model.addAttribute("prodCount", productService.getAllProducts().size());
        model.addAttribute("cateCount", categoryService.getAllCategories().size());
        model.addAttribute("userCount", customerRepo.count());
        model.addAttribute("orderCount", orderRepo.count());

        Double revenue = orderRepo.sumValidRevenue();
        model.addAttribute("revenue", (revenue == null) ? 0.0 : revenue);

        // Review & Order mới
        model.addAttribute("reviewCount", reviewRepo.count());
        Double avgRating = reviewRepo.averageRating();
        model.addAttribute("avgRating", (avgRating == null) ? 0.0 : avgRating);

        List<nvkOrder> recentOrders = orderRepo.findTop5ByOrderByNvkCreatedDateDesc();
        model.addAttribute("recentOrders", (recentOrders == null) ? new ArrayList<>() : recentOrders);

        return "admin/index";
    }

    // Xem Profile cá nhân
    @GetMapping("/profile")
    public String viewProfile(Model model, HttpSession session) {
        nvkAdmin sessionAdmin = (nvkAdmin) session.getAttribute("nvkAdminLogin");
        if (sessionAdmin == null) return "redirect:/nvkAdmin/login";

        nvkAdmin currentAdmin = adminRepository.findById(sessionAdmin.getNvkId()).orElse(null);
        model.addAttribute("nvkAdmin", currentAdmin);

        return "admin/profile";
    }

    // Cập nhật Profile cá nhân
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

            // Xử lý lưu ảnh
            saveAvatarToDrive(currentAdmin, file);

            adminRepository.save(currentAdmin);
            session.setAttribute("nvkAdminLogin", currentAdmin);

            redirectAttributes.addFlashAttribute("successMsg", "Cập nhật hồ sơ thành công! ✅");
        }
        return "redirect:/nvkAdmin/profile";
    }

    // =============================================================
    // PHẦN 2: QUẢN LÝ TÀI KHOẢN ADMIN KHÁC (ACCOUNTS)
    // =============================================================

    // Danh sách Admin
    @GetMapping("/accounts")
    public String listAccounts(Model model) {
        List<nvkAdmin> admins = adminRepository.findAll();
        model.addAttribute("nvkAdmins", admins);
        return "admin/accounts/list";
    }

    // Form Thêm mới
    @GetMapping("/accounts/create")
    public String createAccount(Model model) {
        model.addAttribute("nvkAdmin", new nvkAdmin());
        return "admin/accounts/form";
    }

    // Form Sửa
    @GetMapping("/accounts/edit/{id}")
    public String editAccount(@PathVariable("id") Long id, Model model) {
        nvkAdmin admin = adminRepository.findById(id).orElse(null);
        if (admin == null) return "redirect:/nvkAdmin/accounts";

        model.addAttribute("nvkAdmin", admin);
        return "admin/accounts/form";
    }

    // Lưu Admin
    @PostMapping("/accounts/save")
    public String saveAccount(@ModelAttribute("nvkAdmin") nvkAdmin nvkAdminForm,
                              @RequestParam("nvkImageFile") MultipartFile file,
                              RedirectAttributes redirectAttributes) {
        nvkAdmin currentAdmin;

        if (nvkAdminForm.getNvkId() != null) {
            currentAdmin = adminRepository.findById(nvkAdminForm.getNvkId()).orElse(new nvkAdmin());
        } else {
            currentAdmin = new nvkAdmin();
        }

        currentAdmin.setNvkUsername(nvkAdminForm.getNvkUsername());
        currentAdmin.setNvkFullName(nvkAdminForm.getNvkFullName());
        currentAdmin.setNvkActive(nvkAdminForm.getNvkActive());

        if (nvkAdminForm.getNvkPassword() != null && !nvkAdminForm.getNvkPassword().isEmpty()) {
            currentAdmin.setNvkPassword(nvkAdminForm.getNvkPassword());
        }

        // Xử lý lưu ảnh
        saveAvatarToDrive(currentAdmin, file);

        adminRepository.save(currentAdmin);
        redirectAttributes.addFlashAttribute("successMsg", "Lưu tài khoản thành công! 🎉");

        return "redirect:/nvkAdmin/accounts";
    }

    // Xóa Admin
    @GetMapping("/accounts/delete/{id}")
    public String deleteAccount(@PathVariable("id") Long id,
                                RedirectAttributes redirectAttributes) {
        adminRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMsg", "Đã xóa tài khoản thành công! 🗑️");
        return "redirect:/nvkAdmin/accounts";
    }

    // =============================================================
    // PHẦN 3: HÀM TIỆN ÍCH LƯU ẢNH (DÙNG CHUNG - ĐÃ FIX PATH)
    // =============================================================
    private void saveAvatarToDrive(nvkAdmin admin, MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                // [FIX QUAN TRỌNG]: Tự động lấy đường dẫn dự án hiện tại
                String rootPath = System.getProperty("user.dir");

                // Trỏ vào thư mục uploads/admin nằm trong project
                String uploadDir = rootPath + "/uploads/admin/";

                // Tạo thư mục nếu chưa có
                java.nio.file.Path path = java.nio.file.Paths.get(uploadDir);
                if (!java.nio.file.Files.exists(path)) {
                    java.nio.file.Files.createDirectories(path);
                }

                // Tạo tên file độc nhất
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

                // Lưu file vật lý
                java.nio.file.Path filePath = path.resolve(fileName);
                java.nio.file.Files.copy(file.getInputStream(), filePath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                // Lưu đường dẫn web vào DB (/nvk-images/...)
                admin.setNvkAvatar("/nvk-images/" + fileName);

                System.out.println("-> Đã lưu ảnh tại: " + filePath.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // =============================================================
    // PHẦN 4: CHI TIẾT ĐƠN HÀNG
    // =============================================================
    @GetMapping("/order/detail/{id}")
    public String viewOrderDetail(@PathVariable("id") Long id, Model model) {
        nvkOrder order = orderRepo.findById(id).orElse(null);
        if (order == null) return "redirect:/nvkAdmin/order";

        List<nvkOrderDetail> details = orderDetailRepo.findByNvkOrder(order);
        model.addAttribute("nvkOrder", order);
        model.addAttribute("nvkOrderDetails", details);

        return "admin/order/detail";
    }
}