package com.K23CNT2.Lesson07.nvkService;

import com.K23CNT2.Lesson07.nvkEntity.nvkProduct;
import com.K23CNT2.Lesson07.nvkRepository.nvkProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class nvkProductService {

    @Autowired
    private nvkProductRepository nvkProductRepository;

    // 1. Lấy danh sách sản phẩm
    public List<nvkProduct> nvkGetAllProducts() {
        return nvkProductRepository.findAll();
    }

    // 2. Thêm mới hoặc Sửa sản phẩm
    public void nvkSaveProduct(nvkProduct nvkProduct) {
        nvkProductRepository.save(nvkProduct);
    }

    // 3. Lấy sản phẩm theo ID (Dùng cho chức năng Sửa/Edit)
    public Optional<nvkProduct> nvkGetProductById(Long id) {
        return nvkProductRepository.findById(id);
    }

    // 4. Xóa sản phẩm theo ID (Dùng cho chức năng Xóa/Delete)
    public void nvkDeleteProduct(Long id) {
        nvkProductRepository.deleteById(id);
    }
}