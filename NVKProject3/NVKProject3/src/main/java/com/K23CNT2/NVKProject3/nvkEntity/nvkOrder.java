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

    private String nvkCode;             // Mã đơn (DH001)

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime nvkCreatedDate; // Ngày tạo

    private Double nvkTotalAmount;      // Tổng tiền

    // Thông tin người nhận (đề phòng mua hộ)
    private String nvkReceiverName;
    private String nvkReceiverAddress;
    private String nvkReceiverPhone;

    // 0:Mới, 1:Xác nhận, 2:Đang giao, 3:Hoàn thành, 4:Hủy
    private Integer nvkStatus;

    // Khách nào đặt?
    @ManyToOne
    @JoinColumn(name = "nvk_customer_id")
    @ToString.Exclude
    private nvkCustomer nvkCustomer;

    // Danh sách chi tiết
    @OneToMany(mappedBy = "nvkOrder", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<nvkOrderDetail> nvkOrderDetails;
}