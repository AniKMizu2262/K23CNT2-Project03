package com.K23CNT2.NVKProject3.nvkRepository;

import com.K23CNT2.NVKProject3.nvkEntity.nvkCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface nvkCategoryRepository extends JpaRepository<nvkCategory, Long> {
    List<nvkCategory> findByNvkNameContainingIgnoreCase(String keyword);
}