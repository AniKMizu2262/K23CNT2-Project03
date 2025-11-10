package com.K23CNT2.Lesson04; // Package gốc của bro

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan; // <-- THÊM DÒNG IMPORT NÀY


@ComponentScan(basePackages = {"controller", "service", "exception"})
@SpringBootApplication
public class Lesson04Application {

    public static void main(String[] args) {
        SpringApplication.run(Lesson04Application.class, args);
    }

}