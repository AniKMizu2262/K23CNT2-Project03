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

    @Column(name = "nvk_name")
    private String nvkName;

    // --- Quan hệ (Relations) ---
    @OneToMany(mappedBy = "nvkCategory", fetch = FetchType.LAZY) // 1. Thêm LAZY cho chắc
    @ToString.Exclude
    @EqualsAndHashCode.Exclude // 2. [QUAN TRỌNG] Ngăn Lombok quét qua list này gây chậm
    private List<nvkProduct> nvkProducts;
}