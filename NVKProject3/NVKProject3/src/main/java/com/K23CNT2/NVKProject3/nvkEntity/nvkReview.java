package com.K23CNT2.NVKProject3.nvkEntity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "nvk_reviews")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class nvkReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nvkId;

    private Integer nvkRating; // Số sao (1 đến 5)

    @Column(columnDefinition = "TEXT")
    private String nvkComment; // Bình luận

    private LocalDate nvkCreatedDate; // Ngày viết đánh giá

    // --- Quan hệ ---

    @ManyToOne
    @JoinColumn(name = "nvk_customer_id")
    @ToString.Exclude
    private nvkCustomer nvkCustomer;

    @ManyToOne
    @JoinColumn(name = "nvk_product_id")
    @ToString.Exclude
    private nvkProduct nvkProduct;

    // --- Lifecycle Callbacks ---

    @PrePersist
    public void prePersist() {
        // Tự động set ngày hiện tại khi lưu vào DB
        if (this.nvkCreatedDate == null) {
            this.nvkCreatedDate = LocalDate.now();
        }
    }
}