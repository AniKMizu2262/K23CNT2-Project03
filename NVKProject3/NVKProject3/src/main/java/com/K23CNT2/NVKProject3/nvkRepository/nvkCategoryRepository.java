package com.K23CNT2.NVKProject3.nvkRepository;

import com.K23CNT2.NVKProject3.nvkEntity.nvkCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface nvkCategoryRepository extends JpaRepository<nvkCategory, Long> {

    /**
     * Tìm kiếm danh mục theo tên (Không phân biệt hoa thường)
     *
     * @param keyword Từ khóa tìm kiếm
     */
    List<nvkCategory> findByNvkNameContainingIgnoreCase(String keyword);
}