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

    @Column(unique = true)
    private String nvkEmail;    // Email = Tên đăng nhập

    private String nvkPassword;
    private String nvkFullName;
    private String nvkPhone;
    private String nvkAddress;  // Địa chỉ nhận hàng mặc định
    private String nvkAvatar;

    private Boolean nvkActive;  // Khóa/Mở tài khoản

    // Danh sách đơn hàng của khách
    @OneToMany(mappedBy = "nvkCustomer")
    @ToString.Exclude
    private List<nvkOrder> nvkOrders;
}