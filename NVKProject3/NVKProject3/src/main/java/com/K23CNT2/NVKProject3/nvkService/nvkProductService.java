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

    // === CÁC HÀM CƠ BẢN (CRUD) ===

    /**
     * Lấy tất cả sản phẩm.
     * Sử dụng JOIN FETCH trong Repository để tối ưu hóa hiệu năng (tránh lỗi N+1 query).
     */
    public List<nvkProduct> getAllProducts() {
        return repo.findAllWithCategory();
    }

    public nvkProduct getProductById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public void saveProduct(nvkProduct product) {
        repo.save(product);
    }

    // Hàm save trả về đối tượng (Dùng cho API/Ajax)
    public nvkProduct save(nvkProduct product) {
        return repo.save(product);
    }

    public void updateProduct(nvkProduct product) {
        repo.save(product);
    }

    public void deleteProduct(Long id) {
        repo.deleteById(id);
    }

    // === CHỨC NĂNG CLIENT (LỌC & TÌM KIẾM) ===

    public List<nvkProduct> findRandomProducts() {
        return repo.findRandomProducts();
    }

    /**
     * Bộ lọc sản phẩm tổng hợp (Category, Sort, Stock)
     */
    public List<nvkProduct> filterProducts(Long categoryId, String sortType, boolean inStock) {
        // 1. Xử lý sắp xếp
        Sort sort = Sort.by(Sort.Direction.DESC, "nvkId"); // Mặc định: Mới nhất trước

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

        // 2. Gọi Repository theo điều kiện
        if (categoryId != null) {
            return inStock
                    ? repo.findByNvkCategory_NvkIdAndNvkQuantityGreaterThan(categoryId, 0, sort)
                    : repo.findByNvkCategory_NvkId(categoryId, sort);
        } else {
            return inStock
                    ? repo.findByNvkQuantityGreaterThan(0, sort)
                    : repo.findAll(sort);
        }
    }

    public List<nvkProduct> searchProducts(String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            return repo.findByNvkNameContainingIgnoreCase(keyword);
        }
        return repo.findAllWithCategory();
    }

    /**
     * Tìm kiếm kết hợp lọc danh mục (Dùng cho trang Admin)
     */
    public List<nvkProduct> searchAndFilter(String keyword, Long categoryId) {
        if (keyword != null && !keyword.isEmpty() && categoryId != null) {
            return repo.findByNvkCategory_NvkIdAndNvkNameContainingIgnoreCase(categoryId, keyword);
        } else if (categoryId != null) {
            return repo.findByNvkCategory_NvkId(categoryId);
        } else if (keyword != null && !keyword.isEmpty()) {
            return repo.findByNvkNameContainingIgnoreCase(keyword);
        } else {
            return repo.findAllWithCategory();
        }
    }
}