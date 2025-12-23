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
        // 1. Lấy đường dẫn gốc của dự án
        Path rootPath = Paths.get(System.getProperty("user.dir"));

        // 2. Trỏ tới thư mục uploads
        Path uploadDir = rootPath.resolve("uploads");

        // --- CẤU HÌNH ẢNH USER & ADMIN ---
        // Web gọi /nvk-images/** -> Nó sẽ tìm lần lượt trong 2 thư mục này:
        // 1. uploads/admin/ (Cho Admin)
        // 2. uploads/user/  (Cho Khách hàng - ĐÃ SỬA TỪ 'images' SANG 'user')
        registry.addResourceHandler("/nvk-images/**")
                .addResourceLocations(
                        "file:/" + uploadDir.resolve("admin").toAbsolutePath().toString().replace("\\", "/") + "/",
                        "file:/" + uploadDir.resolve("user").toAbsolutePath().toString().replace("\\", "/") + "/"
                );

        // --- CẤU HÌNH SẢN PHẨM ---
        registry.addResourceHandler("/nvk-product-img/**")
                .addResourceLocations("file:/" + uploadDir.resolve("product").toAbsolutePath().toString().replace("\\", "/") + "/");

        System.out.println("--------------------------------------");
        System.out.println("DA CAU HINH DOC ANH TU:");
        System.out.println("   - Admin:   uploads/admin");
        System.out.println("   - User:    uploads/user");
        System.out.println("   - Product: uploads/product");
        System.out.println("--------------------------------------");
    }
}