package com.K23CNT2.NVKProject3.nvkEntity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "nvk_customers")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class nvkCustomer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nvkId;

    // --- Tài khoản ---
    @Column(unique = true)
    private String nvkEmail;    // Dùng làm tên đăng nhập
    private String nvkPassword;
    private Boolean nvkActive;  // Trạng thái khóa/mở

    // --- Thông tin cá nhân ---
    private String nvkFullName;
    private String nvkPhone;
    private String nvkAddress;
    private String nvkAvatar;

    // --- Quan hệ ---
    @OneToMany(mappedBy = "nvkCustomer")
    @ToString.Exclude
    private List<nvkOrder> nvkOrders;
}