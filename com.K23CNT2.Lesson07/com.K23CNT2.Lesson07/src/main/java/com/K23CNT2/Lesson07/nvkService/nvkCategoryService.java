package com.K23CNT2.Lesson07.nvkService;

import com.K23CNT2.Lesson07.nvkEntity.nvkCategory;
import com.K23CNT2.Lesson07.nvkRepository.nvkCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class nvkCategoryService {

    @Autowired
    private nvkCategoryRepository nvkCategoryRepository;
    // Lưu ý: Tên biến bạn đặt là 'nvkCategoryRepository'

    // 1. Lấy danh sách
    public List<nvkCategory> nvkGetAllCategories() {
        return nvkCategoryRepository.findAll();
    }

    // 2. Thêm mới hoặc cập nhật
    public void nvkSaveCategory(nvkCategory nvkCategory) {
        nvkCategoryRepository.save(nvkCategory);
    }

    // 3. Lấy category theo ID (Cần cho chức năng Sửa/Edit)
    public Optional<nvkCategory> nvkGetCategoryById(Long id) {
        return nvkCategoryRepository.findById(id);
    }

    // 4. Xóa category theo ID (Cần cho chức năng Xóa/Delete)
    public void nvkDeleteCategory(Long id) {
        nvkCategoryRepository.deleteById(id);
    }
}