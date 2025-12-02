package com.K23CNT2.NVKProject3.nvkEntity;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(unique = true)
    private String nvkUsername; // Tên đăng nhập

    private String nvkPassword; // Mật khẩu
    private String nvkFullName; // Tên hiển thị
    private String nvkAvatar;   // Ảnh đại diện

    private Boolean nvkActive;  // Trạng thái hoạt động
}