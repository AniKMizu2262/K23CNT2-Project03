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

    private Integer nvkQuantity; // Số lượng mua
    private Double nvkPrice;     // Giá tại thời điểm chốt đơn

    // Sản phẩm nào?
    @ManyToOne
    @JoinColumn(name = "nvk_product_id")
    @ToString.Exclude
    private nvkProduct nvkProduct;

    // Thuộc đơn nào?
    @ManyToOne
    @JoinColumn(name = "nvk_order_id")
    @ToString.Exclude
    private nvkOrder nvkOrder;
}