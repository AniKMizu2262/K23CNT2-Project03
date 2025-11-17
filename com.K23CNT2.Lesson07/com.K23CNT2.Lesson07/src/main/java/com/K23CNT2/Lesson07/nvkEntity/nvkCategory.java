package com.K23CNT2.Lesson07.nvkEntity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "categories")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class nvkCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nvkId;

    private String nvkCategoryName;
    private Boolean nvkCategoryStatus;


    @OneToMany(mappedBy = "nvkCategory", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<nvkProduct> products;
}