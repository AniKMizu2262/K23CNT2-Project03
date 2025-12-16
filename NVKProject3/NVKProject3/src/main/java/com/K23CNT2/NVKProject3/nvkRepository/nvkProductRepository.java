package com.K23CNT2.NVKProject3.nvkRepository;

import com.K23CNT2.NVKProject3.nvkEntity.nvkProduct;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface nvkProductRepository extends JpaRepository<nvkProduct, Long> {

    // 1. Lấy 8 sản phẩm ngẫu nhiên (Cho Slider)
    @Query(value = "SELECT * FROM nvk_products ORDER BY RAND() LIMIT 8", nativeQuery = true)
    List<nvkProduct> findRandomProducts();

    // 2. Tìm theo Danh mục (Có sắp xếp)
    List<nvkProduct> findByNvkCategory_NvkId(Long categoryId, Sort sort);

    // 3. Tìm theo Danh mục VÀ Số lượng > 0 (Có sắp xếp)
    List<nvkProduct> findByNvkCategory_NvkIdAndNvkQuantityGreaterThan(Long categoryId, Integer quantity, Sort sort);

    // 4. Tìm tất cả SP có số lượng > 0 (Có sắp xếp)
    List<nvkProduct> findByNvkQuantityGreaterThan(Integer quantity, Sort sort);
}