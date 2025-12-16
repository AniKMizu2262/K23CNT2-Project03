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
    private String nvkCode;       // Mã sản phẩm
    private String nvkName;       // Tên sản phẩm
    private String nvkBrand;      // Hãng sản xuất

    // --- Giá & Kho ---
    private Double nvkPrice;      // Giá bán hiện tại
    private Double nvkOldPrice;   // Giá gốc (để gạch ngang)
    private Integer nvkQuantity;  // Số lượng tồn kho

    // --- Chi tiết & Hình ảnh ---
    @Column(length = 1000)
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

    // --- MỚI THÊM: Danh sách đánh giá của sản phẩm này ---
    @OneToMany(mappedBy = "nvkProduct", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<nvkReview> nvkReviews;

    // --- Logic ảo (Computed) ---

    // 1. Tính phần trăm giảm giá
    @Transient
    public Integer getNvkDiscountPercentage() {
        if (nvkOldPrice == null || nvkOldPrice <= 0 || nvkPrice == null) {
            return 0;
        }
        return (int) Math.round(((nvkOldPrice - nvkPrice) / nvkOldPrice) * 100);
    }

    // 2. Tính điểm trung bình (1.0 -> 5.0)
    @Transient
    public Double getNvkAverageRating() {
        if (nvkReviews == null || nvkReviews.isEmpty()) {
            return 0.0;
        }
        double sum = 0;
        for (nvkReview review : nvkReviews) {
            sum += review.getNvkRating();
        }
        // Làm tròn 1 chữ số thập phân (Ví dụ: 4.5)
        return Math.round((sum / nvkReviews.size()) * 10.0) / 10.0;
    }

    // 3. Đếm số lượng đánh giá
    @Transient
    public Integer getNvkReviewCount() {
        return nvkReviews == null ? 0 : nvkReviews.size();
    }
}