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

    // --- FORM LOGIN ---
    @GetMapping("/nvkLogin")
    public String showLoginForm() {
        return "admin/login";
    }

    // --- XỬ LÝ LOGIN ---
    @PostMapping("/nvkLogin")
    public String login(@RequestParam("nvkUsername") String username,
                        @RequestParam("nvkPassword") String password,
                        HttpSession session,
                        Model model) {

        // Kiểm tra thông tin
        nvkAdmin admin = adminService.login(username, password);

        if (admin != null) {
            session.setAttribute("nvkAdminLogin", admin); // Lưu session
            return "redirect:/nvkAdmin";
        } else {
            model.addAttribute("error", "Tài khoản hoặc mật khẩu không đúng!");
            return "admin/login";
        }
    }

    // --- LOGOUT ---
    @GetMapping("/nvkLogout")
    public String logout(HttpSession session) {
        session.invalidate(); // Hủy session
        return "redirect:/nvkLogin";
    }
}