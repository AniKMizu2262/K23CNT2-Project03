package com.K23CNT2.NVKProject3.nvkEntity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "nvk_categories")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class nvkCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nvkId;

    @Column(name = "nvk_name", nullable = false)
    private String nvkName;

    // --- Quan hệ (Relations) ---

    // Sử dụng LAZY để không load toàn bộ sản phẩm khi chỉ cần lấy danh mục
    @OneToMany(mappedBy = "nvkCategory", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude // Ngăn Lombok quét qua list này gây chậm/tràn bộ nhớ
    private List<nvkProduct> nvkProducts;
}