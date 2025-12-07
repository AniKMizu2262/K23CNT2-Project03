package com.K23CNT2.NVKProject3;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminLoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Lấy session hiện tại
        HttpSession session = request.getSession();

        // Kiểm tra session "nvkAdminLogin"
        if (session.getAttribute("nvkAdminLogin") == null) {
            // Chưa đăng nhập -> Đá về trang Login
            response.sendRedirect("/nvkLogin");
            return false; // Chặn request lại
        }

        // Đã đăng nhập -> Cho phép đi tiếp
        return true;
    }
}