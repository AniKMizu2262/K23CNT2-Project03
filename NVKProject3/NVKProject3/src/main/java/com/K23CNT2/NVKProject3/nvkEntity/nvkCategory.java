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

    private String nvkName; // Tên loại: Điện thoại, Laptop...
    // Đã bỏ các trường thừa (Status, Icon) cho nhẹ

    @OneToMany(mappedBy = "nvkCategory")
    @ToString.Exclude // Chặn vòng lặp
    private List<nvkProduct> nvkProducts;
}
