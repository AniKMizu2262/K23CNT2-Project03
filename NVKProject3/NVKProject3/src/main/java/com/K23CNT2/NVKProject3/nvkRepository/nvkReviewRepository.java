package com.K23CNT2.NVKProject3.nvkRepository;

import com.K23CNT2.NVKProject3.nvkEntity.nvkProduct;
import com.K23CNT2.NVKProject3.nvkEntity.nvkReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface nvkReviewRepository extends JpaRepository<nvkReview, Long> {
    // Tìm tất cả đánh giá của 1 sản phẩm cụ thể (để hiển thị ra trang chi tiết)
    List<nvkReview> findByNvkProduct(nvkProduct product);
}