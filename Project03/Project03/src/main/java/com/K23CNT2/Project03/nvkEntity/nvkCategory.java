package com.K23CNT2.Project03.nvkEntity;

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

    private String nvkName;       // Tên loại: Laptop, Điện thoại...
    private Boolean nvkStatus;    // Trạng thái: true-Hiển thị, false-Ẩn

    @OneToMany(mappedBy = "nvkCategory")
    @ToString.Exclude // Chặn vòng lặp vô tận
    private List<nvkProduct> nvkProducts;
}
