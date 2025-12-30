package com.K23CNT2.NVKProject3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class nvkWebConfig implements WebMvcConfigurer {

    @Autowired
    private AdminLoginInterceptor adminLoginInterceptor;

    // --- 1. Cấu hình Đường dẫn đọc ảnh (Static Resources) ---
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Lấy đường dẫn gốc tuyệt đối của dự án để tránh lỗi 404 trên các môi trường khác nhau
        Path rootPath = Paths.get(System.getProperty("user.dir"));
        Path uploadDir = rootPath.resolve("uploads");

        // Cấu hình: Web gọi /images/** -> Tìm trong thư mục uploads/
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:/" + uploadDir.toAbsolutePath().toString().replace("\\", "/") + "/");

        // (Bổ sung nếu muốn dùng chung logic với file kia)
        registry.addResourceHandler("/nvk-images/**")
                .addResourceLocations("file:/" + uploadDir.resolve("admin").toAbsolutePath().toString().replace("\\", "/") + "/",
                        "file:/" + uploadDir.resolve("user").toAbsolutePath().toString().replace("\\", "/") + "/");

        registry.addResourceHandler("/nvk-product-img/**")
                .addResourceLocations("file:/" + uploadDir.resolve("product").toAbsolutePath().toString().replace("\\", "/") + "/");
    }

    // --- 2. Cấu hình Chặn đăng nhập (Interceptor) ---
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminLoginInterceptor)
                .addPathPatterns("/nvkAdmin/**") // Áp dụng cho tất cả trang Admin
                .excludePathPatterns(            // Loại trừ các trang này (để tránh lặp vô tận hoặc lỗi css)
                        "/nvkLogin",
                        "/nvkLogout",
                        "/nvkAdmin/assets/**",
                        "/images/**",
                        "/nvk-images/**",
                        "/nvk-product-img/**",
                        "/css/**",
                        "/js/**"
                );
    }
}