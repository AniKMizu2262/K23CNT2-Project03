package com.K23CNT2.NVKProject3.nvkController;

import com.K23CNT2.NVKProject3.nvkEntity.nvkProduct;
import com.K23CNT2.NVKProject3.nvkService.nvkCategoryService;
import com.K23CNT2.NVKProject3.nvkService.nvkProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.*;
import java.util.List;

@Controller
@RequestMapping("/nvkAdmin/product")
public class nvkProductController {

    @Autowired private nvkProductService productService;
    @Autowired private nvkCategoryService categoryService;

    // 1. Danh sách sản phẩm
    @GetMapping("")
    public String listProduct(Model model) {
        model.addAttribute("nvkProducts", productService.getAllProducts());
        return "admin/product/list";
    }

    // 2. Form thêm mới
    @GetMapping("/create")
    public String createProduct(Model model) {
        model.addAttribute("nvkProduct", new nvkProduct());
        // Gửi danh sách Danh mục sang để hiển thị Dropdown chọn
        model.addAttribute("nvkCategories", categoryService.getAllCategories());
        return "admin/product/form";
    }

    // 3. Form sửa
    @GetMapping("/edit/{id}")
    public String editProduct(@PathVariable("id") Long id, Model model) {
        model.addAttribute("nvkProduct", productService.getProductById(id));
        model.addAttribute("nvkCategories", categoryService.getAllCategories());
        return "admin/product/form";
    }

    // 4. Lưu sản phẩm (Xử lý ảnh + Brand + Giá)
    @PostMapping("/save")
    public String saveProduct(@ModelAttribute("nvkProduct") nvkProduct nvkProduct,
                              @RequestParam("nvkImageFile") MultipartFile file) {

        // --- XỬ LÝ UPLOAD ẢNH (Fix lỗi file đang sử dụng) ---
        if (!file.isEmpty()) {
            try {
                // Tạo tên file ngẫu nhiên để tránh trùng: thời_gian_tên_gốc.jpg
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

                // Đường dẫn lưu: src/main/resources/static/images/
                Path uploadPath = Paths.get("src/main/resources/static/images/");

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                try (InputStream inputStream = file.getInputStream()) {
                    Path filePath = uploadPath.resolve(fileName);
                    // Copy đè nếu cần
                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                }

                // Lưu đường dẫn vào DB
                nvkProduct.setNvkImgUrl("/images/" + fileName);

            } catch (Exception e) {
                e.printStackTrace(); // In lỗi ra console nếu upload xịt
            }
        }
        // ----------------------------------------------------

        productService.saveProduct(nvkProduct);
        return "redirect:/nvkAdmin/product";
    }

    // 5. Xóa sản phẩm
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return "redirect:/nvkAdmin/product";
    }
}