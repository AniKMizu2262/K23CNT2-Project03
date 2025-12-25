package com.K23CNT2.NVKProject3.nvkEntity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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
    private String nvkCode;       // Mã sản phẩm (SKU)
    private String nvkName;       // Tên sản phẩm
    private String nvkBrand;      // Thương hiệu

    // --- Giá & Kho ---
    private Double nvkPrice;      // Giá bán thực tế
    private Double nvkOldPrice;   // Giá gốc (để hiển thị gạch ngang)
    private Integer nvkQuantity;  // Tồn kho

    // --- Chi tiết & Hình ảnh ---
    @Column(length = 1000)
    private String nvkImgUrl;     // Link ảnh

    @Column(columnDefinition = "TEXT")
    private String nvkDescription;

    // --- Trạng thái ---
    // 0: Ngừng kinh doanh | 1: Đang bán | 2: Sắp về
    private Integer nvkStatus;

    // --- Quan hệ ---

    @ManyToOne
    @JoinColumn(name = "nvk_cate_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private nvkCategory nvkCategory;

    @OneToMany(mappedBy = "nvkProduct", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<nvkReview> nvkReviews;

    // --- Computed Properties (Tính toán ảo, không lưu DB) ---

    // 1. Tính % giảm giá (VD: Giảm 20%)
    @Transient
    public Integer getNvkDiscountPercentage() {
        if (nvkOldPrice == null || nvkOldPrice <= 0 || nvkPrice == null) {
            return 0;
        }
        return (int) Math.round(((nvkOldPrice - nvkPrice) / nvkOldPrice) * 100);
    }

    // 2. Tính điểm đánh giá trung bình (VD: 4.5 sao)
    @Transient
    public Double getNvkAverageRating() {
        if (nvkReviews == null || nvkReviews.isEmpty()) {
            return 0.0;
        }
        double sum = 0;
        for (nvkReview review : nvkReviews) {
            sum += review.getNvkRating();
        }
        return Math.round((sum / nvkReviews.size()) * 10.0) / 10.0;
    }

    // 3. Đếm số lượng đánh giá
    @Transient
    public Integer getNvkReviewCount() {
        return nvkReviews == null ? 0 : nvkReviews.size();
    }
}