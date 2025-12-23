package com.K23CNT2.NVKProject3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class nvkWebConfig implements WebMvcConfigurer {

    @Autowired
    private AdminLoginInterceptor adminLoginInterceptor;

    // --- 1. Cấu hình Static Resources ---
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Lấy đường dẫn tuyệt đối của thư mục uploads
        String path = Paths.get("./uploads").toUri().toString();

        // Cấu hình map URL
        registry.addResourceHandler("/images/**")
                .addResourceLocations(path);
    }

    // --- 2. Cấu hình Interceptor ---
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminLoginInterceptor)
                .addPathPatterns("/nvkAdmin/**")
                .excludePathPatterns("/nvkLogin/**", "/nvkAdmin/assets/**");
    }
}