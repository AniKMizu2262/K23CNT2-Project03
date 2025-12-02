package com.K23CNT2.Project03.nvkEntity;

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

    private String nvkCode;       // Mã SP (IP15, DELL_XPS...)
    private String nvkName;       // Tên SP
    private Double nvkPrice;      // Giá bán thực tế
    private Double nvkOldPrice;   // Giá cũ (để gạch đi)
    private String nvkImgUrl;     // Ảnh chính
    private Integer nvkQuantity;  // Số lượng tồn kho

    @Column(columnDefinition = "TEXT")
    private String nvkDescription; // Lưu cấu hình chi tiết (HTML/Text)

    // Trạng thái: 0-Ngừng KD, 1-Đang bán, 2-Sắp về
    private Integer nvkStatus;

    // Liên kết sang Danh mục
    @ManyToOne
    @JoinColumn(name = "nvk_cate_id")
    @ToString.Exclude
    private nvkCategory nvkCategory;

    // === HÀM ẢO TÍNH % GIẢM GIÁ (Không tạo cột trong DB) ===
    @Transient
    public Integer getNvkDiscountPercentage() {
        if (nvkOldPrice == null || nvkOldPrice <= 0 || nvkPrice == null) {
            return 0;
        }
        double discount = ((nvkOldPrice - nvkPrice) / nvkOldPrice) * 100;
        return (int) Math.round(discount);
    }
}
