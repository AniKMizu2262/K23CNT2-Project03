package com.K23CNT2.NVKProject3.nvkService;

import com.K23CNT2.NVKProject3.nvkEntity.nvkCategory;
import com.K23CNT2.NVKProject3.nvkRepository.nvkCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class nvkCategoryService {

    @Autowired
    private nvkCategoryRepository repo;

    // Lấy tất cả danh mục
    public List<nvkCategory> getAllCategories() {
        return repo.findAll();
    }

    // Lấy chi tiết danh mục theo ID
    public nvkCategory getCategoryById(Long id) {
        return repo.findById(id).orElse(null);
    }

    // Thêm mới hoặc Cập nhật danh mục
    public void saveCategory(nvkCategory category) {
        repo.save(category);
    }

    // Xóa danh mục
    public void deleteCategory(Long id) {
        repo.deleteById(id);
    }

    // Tìm kiếm danh mục theo tên
    public List<nvkCategory> searchCategories(String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            return repo.findByNvkNameContainingIgnoreCase(keyword);
        }
        return repo.findAll();
    }
}