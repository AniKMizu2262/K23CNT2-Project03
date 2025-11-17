package com.K23CNT2.Lesson07.nvkEntity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class nvkProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nvkId;

    // Sửa tên biến cho chuẩn: Tên sản phẩm là nvkProductName
    private String nvkProductName;

    // Sửa tên biến: Giá sản phẩm là nvkPrice
    private Double nvkPrice;

    @ManyToOne
    @JoinColumn(name = "nvkCategory_id")
    @ToString.Exclude // <--- QUAN TRỌNG: Không có dòng này là lỗi 500 ngay lập tức!
    private nvkCategory nvkCategory;
}