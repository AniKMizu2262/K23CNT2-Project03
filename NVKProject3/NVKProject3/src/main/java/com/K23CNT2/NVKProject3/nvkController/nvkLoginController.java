package com.K23CNT2.NVKProject3.nvkController;

import com.K23CNT2.NVKProject3.nvkEntity.nvkAdmin;
import com.K23CNT2.NVKProject3.nvkService.nvkAdminService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class nvkLoginController {

    @Autowired
    private nvkAdminService adminService;

    // 1. Form Đăng nhập
    @GetMapping("/nvkLogin")
    public String showLoginForm() {
        return "admin/login"; // Tạo file login.html trong thư mục admin
    }

    // 2. Xử lý Đăng nhập
    @PostMapping("/nvkLogin")
    public String login(@RequestParam("nvkUsername") String username,
                        @RequestParam("nvkPassword") String password,
                        HttpSession session,
                        Model model) {

        // Kiểm tra tài khoản trong DB
        nvkAdmin admin = adminService.login(username, password);

        if (admin != null) {
            // Lưu phiên đăng nhập
            session.setAttribute("nvkAdminLogin", admin);
            return "redirect:/nvkAdmin"; // Vào trang chủ Admin
        } else {
            // Báo lỗi
            model.addAttribute("error", "Tài khoản hoặc mật khẩu không đúng!");
            return "admin/login";
        }
    }

    // 3. Đăng xuất
    @GetMapping("/nvkLogout")
    public String logout(HttpSession session) {
        session.invalidate(); // Xóa session
        return "redirect:/nvkLogin";
    }
}