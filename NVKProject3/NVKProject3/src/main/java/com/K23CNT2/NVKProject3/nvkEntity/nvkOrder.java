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
    private String nvkCode;             // Mã đơn hàng
    private Double nvkTotalAmount;      // Tổng tiền

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime nvkCreatedDate; // Ngày đặt

    // --- Thông tin người nhận ---
    private String nvkReceiverName;
    private String nvkReceiverPhone;
    private String nvkReceiverAddress;

    // --- Trạng thái (0:Mới, 1:Xác nhận, 2:Giao, 3:Xong, 4:Hủy) ---
    private Integer nvkStatus;

    // --- Quan hệ ---
    @ManyToOne
    @JoinColumn(name = "nvk_customer_id")
    @ToString.Exclude
    private nvkCustomer nvkCustomer;

    @OneToMany(mappedBy = "nvkOrder", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<nvkOrderDetail> nvkOrderDetails;
}