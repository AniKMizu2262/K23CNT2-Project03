package com.K23CNT2.NVKProject3.nvkRepository;

import com.K23CNT2.NVKProject3.nvkEntity.nvkProduct;
import com.K23CNT2.NVKProject3.nvkEntity.nvkReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface nvkReviewRepository extends JpaRepository<nvkReview, Long> {

    // Lấy đánh giá của một sản phẩm (Cho trang Detail)
    List<nvkReview> findByNvkProduct(nvkProduct product);

    /**
     * Tính điểm đánh giá trung bình của toàn hệ thống (Cho Dashboard)
     */
    @Query("SELECT AVG(r.nvkRating) FROM nvkReview r")
    Double averageRating();

    /**
     * Tìm kiếm và lọc đánh giá (Admin)
     *
     * @param rating  Số sao (1-5)
     * @param keyword Nội dung hoặc Tên sản phẩm
     */
    @Query("SELECT r FROM nvkReview r WHERE " +
            "(:rating IS NULL OR r.nvkRating = :rating) AND " +
            "(:keyword IS NULL OR r.nvkComment LIKE %:keyword% OR r.nvkProduct.nvkName LIKE %:keyword%) " +
            "ORDER BY r.nvkCreatedDate DESC")
    List<nvkReview> findByRatingAndKeyword(@Param("rating") Integer rating, @Param("keyword") String keyword);
}