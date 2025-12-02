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

    private String nvkCode;       // Mã SP (IP15, DELL...)
    private String nvkName;       // Tên SP
    private String nvkBrand;      // Hãng (Apple, Samsung...) - MỚI THÊM

    private Double nvkPrice;      // Giá bán
    private Double nvkOldPrice;   // Giá cũ
    private String nvkImgUrl;     // Ảnh
    private Integer nvkQuantity;  // Tồn kho

    @Column(columnDefinition = "TEXT")
    private String nvkDescription; // Cấu hình chi tiết (HTML/Text)

    // 0: Ngừng bán, 1: Đang bán, 2: Sắp về
    private Integer nvkStatus;

    // Liên kết sang Danh mục
    @ManyToOne
    @JoinColumn(name = "nvk_cate_id")
    @ToString.Exclude
    private nvkCategory nvkCategory;

    // === Hàm ảo tính % giảm giá (Không lưu DB) ===
    @Transient
    public Integer getNvkDiscountPercentage() {
        if (nvkOldPrice == null || nvkOldPrice <= 0 || nvkPrice == null) return 0;
        return (int) Math.round(((nvkOldPrice - nvkPrice) / nvkOldPrice) * 100);
    }
}