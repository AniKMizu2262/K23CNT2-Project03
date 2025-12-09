package com.K23CNT2.NVKProject3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class nvkWebConfig implements WebMvcConfigurer {

    @Autowired
    private AdminLoginInterceptor adminLoginInterceptor;

    // --- 1. Cấu hình Static Resources (Đã nâng cấp) ---
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:src/main/resources/static/images/", "classpath:/static/images/");
    }

    // --- 2. Cấu hình Interceptor (Bảo mật Admin) ---
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminLoginInterceptor)
                .addPathPatterns("/nvkAdmin/**")              // Chặn toàn bộ trang Admin
                .excludePathPatterns("/nvkLogin/**", "/nvkAdmin/assets/**"); // Trừ trang Login và CSS/JS
    }
}