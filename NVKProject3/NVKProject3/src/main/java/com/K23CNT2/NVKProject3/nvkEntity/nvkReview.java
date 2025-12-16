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

    private Integer nvkRating; // Điểm đánh giá (1-5)

    @Column(columnDefinition = "TEXT")
    private String nvkComment; // Nội dung bình luận

    private LocalDate nvkCreatedDate; // Ngày đánh giá

    // --- Quan hệ ---

    // Nối với Khách hàng (nvkCustomer)
    @ManyToOne
    @JoinColumn(name = "nvk_customer_id")
    @ToString.Exclude
    private nvkCustomer nvkCustomer;

    // Nối với Sản phẩm (nvkProduct)
    @ManyToOne
    @JoinColumn(name = "nvk_product_id")
    @ToString.Exclude
    private nvkProduct nvkProduct;

    // Tự động lấy ngày hiện tại khi lưu
    @PrePersist
    public void prePersist() {
        this.nvkCreatedDate = LocalDate.now();
    }
}