package com.K23CNT2.NVKProject3.nvkEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "nvk_admins")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class nvkAdmin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nvkId;

    // --- Thông tin đăng nhập ---
    @Column(unique = true, nullable = false)
    private String nvkUsername;

    @Column(nullable = false)
    private String nvkPassword;

    // --- Thông tin cá nhân ---
    private String nvkFullName;
    private String nvkAvatar;

    // --- Trạng thái (true: Hoạt động, false: Khóa) ---
    private Boolean nvkActive;
}