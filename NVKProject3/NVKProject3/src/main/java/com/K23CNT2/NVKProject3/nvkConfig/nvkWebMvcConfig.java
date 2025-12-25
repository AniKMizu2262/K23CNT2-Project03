package com.K23CNT2.NVKProject3.nvkConfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class nvkWebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 1. Lấy đường dẫn gốc tuyệt đối của dự án (Dynamic Path)
        Path rootPath = Paths.get(System.getProperty("user.dir"));
        Path uploadDir = rootPath.resolve("uploads");

        // ========================================================================
        // CẤU HÌNH MAPPING ĐƯỜNG DẪN TĨNH (STATIC RESOURCES)
        // ========================================================================

        // 1. Cấu hình ảnh Avatar (Admin & User)
        // Logic: Web gọi /nvk-images/** -> Spring tìm file vật lý trong uploads/admin/ HOẶC uploads/user/
        registry.addResourceHandler("/nvk-images/**")
                .addResourceLocations(
                        "file:/" + uploadDir.resolve("admin").toAbsolutePath().toString().replace("\\", "/") + "/",
                        "file:/" + uploadDir.resolve("user").toAbsolutePath().toString().replace("\\", "/") + "/"
                );

        // 2. Cấu hình ảnh Sản phẩm
        // Logic: Web gọi /nvk-product-img/** -> Spring tìm file vật lý trong uploads/product/
        registry.addResourceHandler("/nvk-product-img/**")
                .addResourceLocations(
                        "file:/" + uploadDir.resolve("product").toAbsolutePath().toString().replace("\\", "/") + "/"
                );

        // In log ra console để kiểm tra đường dẫn (Dùng cho Debug)
        System.out.println("=======================================================");
        System.out.println("[CONFIG] CẤU HÌNH ĐỌC ẢNH TĨNH THÀNH CÔNG:");
        System.out.println("   - Admin/User Avatar:  " + uploadDir.resolve("admin").toAbsolutePath());
        System.out.println("   - Product Images:     " + uploadDir.resolve("product").toAbsolutePath());
        System.out.println("=======================================================");
    }
}