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

    // Hiển thị form đăng nhập
    @GetMapping("/nvkLogin")
    public String showLoginForm() {
        return "admin/login";
    }

    // Xử lý đăng nhập
    @PostMapping("/nvkLogin")
    public String login(@RequestParam("nvkUsername") String username,
                        @RequestParam("nvkPassword") String password,
                        HttpSession session,
                        Model model) {

        nvkAdmin admin = adminService.login(username, password);

        if (admin != null) {
            session.setAttribute("nvkAdminLogin", admin); // Lưu session admin
            return "redirect:/nvkAdmin";
        } else {
            model.addAttribute("error", "Sai tài khoản hoặc mật khẩu!");
            return "admin/login";
        }
    }

    // Xử lý đăng xuất
    @GetMapping("/nvkLogout")
    public String logout(HttpSession session) {
        session.invalidate(); // Hủy toàn bộ session
        return "redirect:/nvkLogin";
    }
}