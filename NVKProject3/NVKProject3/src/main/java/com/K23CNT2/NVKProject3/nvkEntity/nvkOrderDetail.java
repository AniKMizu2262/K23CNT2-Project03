package com.K23CNT2.NVKProject3.nvkEntity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "nvk_order_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class nvkOrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nvkId;

    // --- Thông tin chi tiết tại thời điểm mua ---
    private Integer nvkQuantity;
    private Double nvkPrice;     // Giá này được lưu cứng, phòng trường hợp sau này giá SP thay đổi

    // --- Quan hệ ---
    @ManyToOne
    @JoinColumn(name = "nvk_product_id")
    @ToString.Exclude
    private nvkProduct nvkProduct;

    @ManyToOne
    @JoinColumn(name = "nvk_order_id")
    @ToString.Exclude
    private nvkOrder nvkOrder;
}