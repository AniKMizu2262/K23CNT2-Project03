package com.K23CNT2.NVKProject3.nvkService;

import com.K23CNT2.NVKProject3.nvkEntity.nvkProduct;
import com.K23CNT2.NVKProject3.nvkRepository.nvkProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class nvkProductService {

    @Autowired
    private nvkProductRepository repo;

    // --- Lấy danh sách ---
    public List<nvkProduct> getAllProducts() {
        return repo.findAll();
    }

    // --- Lấy chi tiết ---
    public nvkProduct getProductById(Long id) {
        return repo.findById(id).orElse(null);
    }

    // --- Thêm / Sửa ---
    public void saveProduct(nvkProduct product) {
        repo.save(product);
    }

    // --- Xóa ---
    public void deleteProduct(Long id) {
        repo.deleteById(id);
    }
}