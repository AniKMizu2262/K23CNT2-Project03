package com.K23CNT2.NVKProject3.nvkService;

import com.K23CNT2.NVKProject3.nvkEntity.nvkProduct;
import com.K23CNT2.NVKProject3.nvkRepository.nvkProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class nvkProductService {

    @Autowired
    private nvkProductRepository repo;

    // === CÁC HÀM CƠ BẢN ===

    public List<nvkProduct> getAllProducts() {
        // [FIX LAG Ở ĐÂY]: Thay vì dùng repo.findAll() (bị chậm), ta dùng hàm tối ưu:
        return repo.findAllWithCategory();
    }

    public nvkProduct getProductById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public void saveProduct(nvkProduct product) {
        repo.save(product);
    }

    public void updateProduct(nvkProduct product) {
        repo.save(product);
    }

    public void deleteProduct(Long id) {
        repo.deleteById(id);
    }

    // === CHỨC NĂNG NÂNG CAO (LỌC & TÌM KIẾM) ===

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

    // === 2 HÀM CỦA BRO ===

    public List<nvkProduct> searchProducts(String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            return repo.findByNvkNameContainingIgnoreCase(keyword);
        }
        return repo.findAllWithCategory(); // Search rỗng thì trả về list tối ưu
    }

    public nvkProduct save(nvkProduct product) {
        return repo.save(product);
    }

    public List<nvkProduct> searchAndFilter(String keyword, Long categoryId) {
        if (keyword != null && !keyword.isEmpty() && categoryId != null) {
            return repo.findByNvkCategory_NvkIdAndNvkNameContainingIgnoreCase(categoryId, keyword);
        } else if (categoryId != null) {
            return repo.findByNvkCategory_NvkId(categoryId);
        } else if (keyword != null && !keyword.isEmpty()) {
            return repo.findByNvkNameContainingIgnoreCase(keyword);
        } else {
            return repo.findAllWithCategory(); // Lấy hết thì dùng hàm tối ưu
        }
    }
}