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
    @Column(unique = true, nullable = false)
    private String nvkEmail;    // Tên đăng nhập
    private String nvkPassword;
    private Boolean nvkActive;  // Trạng thái hoạt động

    // --- Thông tin cá nhân ---
    private String nvkFullName;
    private String nvkPhone;
    private String nvkAddress;
    private String nvkAvatar;

    // --- Quan hệ ---
    @OneToMany(mappedBy = "nvkCustomer", fetch = FetchType.LAZY)
    @ToString.Exclude // Ngắt vòng lặp khi in log
    @EqualsAndHashCode.Exclude
    private List<nvkOrder> nvkOrders;
}