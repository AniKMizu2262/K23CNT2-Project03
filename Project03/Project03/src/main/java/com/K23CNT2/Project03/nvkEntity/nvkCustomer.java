package com.K23CNT2.Project03.nvkEntity;

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

    @Column(unique = true)
    private String nvkEmail;    // Email làm tên đăng nhập

    private String nvkPassword; // Mật khẩu
    private String nvkFullName; // Họ tên
    private String nvkPhone;    // Số điện thoại
    private String nvkAddress;  // Địa chỉ nhận hàng
    private String nvkAvatar;   // Ảnh đại diện

    private Boolean nvkActive;  // Khóa tài khoản (true=Mở, false=Khóa)

    // Một khách hàng có nhiều đơn hàng
    @OneToMany(mappedBy = "nvkCustomer")
    @ToString.Exclude
    private List<nvkOrder> nvkOrders;
}
