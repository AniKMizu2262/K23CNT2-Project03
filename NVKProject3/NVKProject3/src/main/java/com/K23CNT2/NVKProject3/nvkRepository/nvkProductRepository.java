package com.K23CNT2.NVKProject3.nvkRepository;

import com.K23CNT2.NVKProject3.nvkEntity.nvkProduct;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface nvkProductRepository extends JpaRepository<nvkProduct, Long> {

    // === 1. CÁC HÀM CƠ BẢN (JPA AUTO) ===

    // Tìm theo Danh mục (Có sắp xếp)
    List<nvkProduct> findByNvkCategory_NvkId(Long categoryId);

    List<nvkProduct> findByNvkCategory_NvkId(Long categoryId, Sort sort);

    // Tìm theo Danh mục + Còn hàng
    List<nvkProduct> findByNvkCategory_NvkIdAndNvkQuantityGreaterThan(Long categoryId, Integer quantity, Sort sort);

    // Tìm tất cả SP còn hàng
    List<nvkProduct> findByNvkQuantityGreaterThan(Integer quantity, Sort sort);

    // Tìm theo tên (Search cơ bản)
    List<nvkProduct> findByNvkNameContainingIgnoreCase(String name);

    // Tìm kết hợp Danh mục + Tên (Search nâng cao)
    List<nvkProduct> findByNvkCategory_NvkIdAndNvkNameContainingIgnoreCase(Long categoryId, String name);

    // === 2. CUSTOM QUERIES (NATIVE & JPQL) ===

    /**
     * Lấy 8 sản phẩm ngẫu nhiên để hiển thị Slider/Gợi ý.
     * Sử dụng Native Query để tận dụng hàm RAND() của MySQL.
     */
    @Query(value = "SELECT * FROM nvk_products ORDER BY RAND() LIMIT 8", nativeQuery = true)
    List<nvkProduct> findRandomProducts();

    /**
     * [OPTIMIZED] Lấy tất cả sản phẩm kèm theo thông tin Danh mục (JOIN FETCH).
     * Giúp tránh lỗi N+1 Query (Hibernate không phải query lại bảng Category cho từng sản phẩm).
     */
    @Query("SELECT p FROM nvkProduct p LEFT JOIN FETCH p.nvkCategory")
    List<nvkProduct> findAllWithCategory();
}