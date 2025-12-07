package com.K23CNT2.NVKProject3.nvkEntity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "nvk_products")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class nvkProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nvkId;

    // --- Thông tin cơ bản ---
    private String nvkCode;       // Mã sản phẩm
    private String nvkName;       // Tên sản phẩm
    private String nvkBrand;      // Hãng sản xuất

    // --- Giá & Kho ---
    private Double nvkPrice;      // Giá bán hiện tại
    private Double nvkOldPrice;   // Giá gốc (để gạch ngang)
    private Integer nvkQuantity;  // Số lượng tồn kho

    // --- Chi tiết & Hình ảnh ---
    private String nvkImgUrl;     // Đường dẫn ảnh

    @Column(columnDefinition = "TEXT")
    private String nvkDescription; // Mô tả chi tiết

    // --- Trạng thái (0: Ngừng, 1: Bán, 2: Sắp về) ---
    private Integer nvkStatus;

    // --- Quan hệ ---
    @ManyToOne
    @JoinColumn(name = "nvk_cate_id")
    @ToString.Exclude
    private nvkCategory nvkCategory;

    // --- Logic ảo (Computed) ---
    @Transient
    public Integer getNvkDiscountPercentage() {
        if (nvkOldPrice == null || nvkOldPrice <= 0 || nvkPrice == null) {
            return 0;
        }
        return (int) Math.round(((nvkOldPrice - nvkPrice) / nvkOldPrice) * 100);
    }
}