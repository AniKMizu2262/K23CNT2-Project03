package com.K23CNT2.NVKProject3.nvkService;

import com.K23CNT2.NVKProject3.nvkEntity.nvkProduct;
import com.K23CNT2.NVKProject3.nvkRepository.nvkProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class nvkProductService {

    // === 1. KHAI BÁO REPO LÊN ĐẦU (ĐỂ CODE GỌN GÀNG) ===
    @Autowired
    private nvkProductRepository repo;

    // === 2. HÀM UPDATE MÀ CONTROLLER ĐANG CẦN ===
    public void updateProduct(nvkProduct product) {
        repo.save(product); //
    }

    // ==============================================================
    // 3. CÁC HÀM CRUD CƠ BẢN
    // ==============================================================

    public List<nvkProduct> getAllProducts() {
        return repo.findAll();
    }

    public nvkProduct getProductById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public void saveProduct(nvkProduct product) {
        repo.save(product);
    }

    public void deleteProduct(Long id) {
        repo.deleteById(id);
    }

    // ==============================================================
    // 4. CHỨC NĂNG ĐẶC BIỆT (Slider, Filter...)
    // ==============================================================

    public List<nvkProduct> findRandomProducts() {

        return repo.findRandomProducts();
    }

    public List<nvkProduct> filterProducts(Long categoryId, String sortType, boolean inStock) {
        // --- BƯỚC 1: XỬ LÝ SORT ---
        Sort sort = Sort.by(Sort.Direction.DESC, "nvkId");

        if (sortType != null) {
            switch (sortType) {
                case "price-asc":
                    sort = Sort.by(Sort.Direction.ASC, "nvkPrice");
                    break;
                case "price-desc":
                    sort = Sort.by(Sort.Direction.DESC, "nvkPrice");
                    break;
                case "name-asc":
                    sort = Sort.by(Sort.Direction.ASC, "nvkName");
                    break;
                case "name-desc":
                    sort = Sort.by(Sort.Direction.DESC, "nvkName");
                    break;
            }
        }

        // --- BƯỚC 2: GỌI REPO ---
        // Lưu ý: Bro phải đảm bảo Repository đã có các hàm findBy... bên dưới
        if (categoryId != null) {
            if (inStock) {
                return repo.findByNvkCategory_NvkIdAndNvkQuantityGreaterThan(categoryId, 0, sort);
            } else {
                return repo.findByNvkCategory_NvkId(categoryId, sort);
            }
        } else {
            if (inStock) {
                return repo.findByNvkQuantityGreaterThan(0, sort);
            } else {
                return repo.findAll(sort);
            }
        }
    }
}