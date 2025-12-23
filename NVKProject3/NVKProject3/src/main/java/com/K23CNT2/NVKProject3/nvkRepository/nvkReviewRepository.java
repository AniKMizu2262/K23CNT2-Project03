package com.K23CNT2.NVKProject3.nvkRepository;

import com.K23CNT2.NVKProject3.nvkEntity.nvkProduct;
import com.K23CNT2.NVKProject3.nvkEntity.nvkReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface nvkReviewRepository extends JpaRepository<nvkReview, Long> {
    // Tìm tất cả đánh giá của 1 sản phẩm cụ thể (để hiển thị ra trang chi tiết)
    List<nvkReview> findByNvkProduct(nvkProduct product);

    // Tính điểm trung bình sao
    @Query("SELECT AVG(r.nvkRating) FROM nvkReview r")
    Double averageRating();

    @Query("SELECT r FROM nvkReview r WHERE " +
            "(:rating IS NULL OR r.nvkRating = :rating) AND " +
            "(:keyword IS NULL OR r.nvkComment LIKE %:keyword% OR r.nvkProduct.nvkName LIKE %:keyword%) " +
            "ORDER BY r.nvkCreatedDate DESC")
    List<nvkReview> findByRatingAndKeyword(@Param("rating") Integer rating, @Param("keyword") String keyword);
}