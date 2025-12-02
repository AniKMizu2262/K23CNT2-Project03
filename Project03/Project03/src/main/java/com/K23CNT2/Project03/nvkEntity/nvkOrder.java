package com.K23CNT2.Project03.nvkEntity;

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

    private String nvkCode;             // Mã đơn hàng (VD: DH001)

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime nvkCreatedDate; // Ngày đặt hàng

    private Double nvkTotalAmount;      // Tổng tiền đơn hàng

    // Thông tin người nhận (Lưu riêng đề phòng khách mua hộ người khác)
    private String nvkReceiverName;
    private String nvkReceiverAddress;
    private String nvkReceiverPhone;

    // 0:Mới đặt, 1:Đã xác nhận, 2:Đang giao, 3:Đã giao, 4:Hủy
    private Integer nvkStatus;

    // Đơn hàng thuộc về Khách hàng nào?
    @ManyToOne
    @JoinColumn(name = "nvk_customer_id")
    @ToString.Exclude
    private nvkCustomer nvkCustomer;

    // Danh sách chi tiết món hàng trong đơn
    @OneToMany(mappedBy = "nvkOrder", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<nvkOrderDetail> nvkOrderDetails;
}
