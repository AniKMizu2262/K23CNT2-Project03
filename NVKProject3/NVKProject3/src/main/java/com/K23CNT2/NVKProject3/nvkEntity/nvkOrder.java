package com.K23CNT2.NVKProject3.nvkEntity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "nvk_orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class nvkOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nvkId;

    // --- Thông tin chung ---
    private String nvkCode;             // Mã đơn hàng (VD: ORD-12345)
    private Double nvkTotalAmount;      // Tổng tiền thanh toán

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime nvkCreatedDate; // Ngày tạo đơn

    // --- Thông tin người nhận ---
    private String nvkReceiverName;
    private String nvkReceiverPhone;
    private String nvkReceiverAddress;

    // --- Trạng thái đơn hàng ---
    // 0: Mới đặt (Chờ xác nhận)
    // 1: Đã xác nhận
    // 2: Đang giao hàng
    // 3: Đã giao thành công
    // 4: Đã hủy
    private Integer nvkStatus;

    // --- Quan hệ ---
    @ManyToOne
    @JoinColumn(name = "nvk_customer_id")
    @ToString.Exclude
    private nvkCustomer nvkCustomer;

    @OneToMany(mappedBy = "nvkOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<nvkOrderDetail> nvkOrderDetails;
}