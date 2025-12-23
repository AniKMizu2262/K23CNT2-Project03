package com.K23CNT2.NVKProject3.nvkService;

import com.K23CNT2.NVKProject3.nvkEntity.nvkCategory;
import com.K23CNT2.NVKProject3.nvkRepository.nvkCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class nvkCategoryService {

    // LỖI 1 ĐÃ SỬA: Bỏ chữ "static" đi. @Autowired chỉ tiêm vào biến thường.
    @Autowired
    private nvkCategoryRepository repo;

    // --- Lấy danh sách ---
    public List<nvkCategory> getAllCategories() {
        return repo.findAll();
    }

    // --- Lấy chi tiết ---
    public nvkCategory getCategoryById(Long id) {
        return repo.findById(id).orElse(null);
    }

    // --- Thêm / Sửa ---
    public void saveCategory(nvkCategory category) {
        repo.save(category);
    }

    // --- Xóa ---
    public void deleteCategory(Long id) {
        repo.deleteById(id);
    }

    // LỖI 2 & 3 ĐÃ SỬA: Bỏ "static" ở tên hàm và bỏ dòng @Autowired vô duyên ở trong
    public List<nvkCategory> searchCategories(String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            return repo.findByNvkNameContainingIgnoreCase(keyword);
        }
        return repo.findAll();
    }
}