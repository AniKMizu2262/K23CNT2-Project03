package com.K23CNT2.NVKProject3;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Interceptor: Chặn các request vào đường dẫn /nvkAdmin/**
 * Nếu chưa đăng nhập (Session null) -> Đá về trang Login
 */
@Component
public class AdminLoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Lấy session hiện tại
        HttpSession session = request.getSession();

        // Kiểm tra xem đã có Admin trong session chưa
        if (session.getAttribute("nvkAdminLogin") == null) {
            // Chưa đăng nhập -> Chuyển hướng về trang Login
            response.sendRedirect("/nvkLogin");
            return false; // Chặn không cho đi tiếp
        }

        // Đã đăng nhập -> Cho phép đi tiếp vào Controller
        return true;
    }
}