package com.K23CNT2.NVKProject3.nvkController;

import com.K23CNT2.NVKProject3.nvkEntity.nvkCategory;
import com.K23CNT2.NVKProject3.nvkEntity.nvkProduct;
import com.K23CNT2.NVKProject3.nvkService.nvkCategoryService;
import com.K23CNT2.NVKProject3.nvkService.nvkProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
@RequestMapping("/nvkAdmin/product")
public class nvkProductController {

    @Autowired
    private nvkProductService productService;
    @Autowired
    private nvkCategoryService categoryService;

    // 1. HIỂN THỊ DANH SÁCH
    @GetMapping("")
    public String listProduct(Model model,
                              @RequestParam(value = "keyword", required = false) String keyword,
                              @RequestParam(value = "categoryId", required = false) Long categoryId) {
        List<nvkProduct> listProducts = productService.searchAndFilter(keyword, categoryId);
        List<nvkCategory> listCategories = categoryService.getAllCategories();

        model.addAttribute("nvkProducts", listProducts);
        model.addAttribute("nvkCategories", listCategories);
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);

        return "admin/product/list";
    }

    // 2. FORM THÊM MỚI
    @GetMapping("/create")
    public String createProduct(Model model) {
        model.addAttribute("nvkProduct", new nvkProduct());
        model.addAttribute("nvkCategories", categoryService.getAllCategories());
        return "admin/product/form";
    }

    // 3. FORM SỬA
    @GetMapping("/edit/{id}")
    public String editProduct(@PathVariable("id") Long id, Model model) {
        nvkProduct product = productService.getProductById(id);
        if (product == null) return "redirect:/nvkAdmin/product";

        model.addAttribute("nvkProduct", product);
        model.addAttribute("nvkCategories", categoryService.getAllCategories());
        return "admin/product/form";
    }

    // 4. CHỨC NĂNG LƯU (ĐÃ FIX LỖI PATH)
    @PostMapping("/save")
    public String saveProduct(@ModelAttribute("nvkProduct") nvkProduct nvkProduct,
                              @RequestParam(value = "nvkImageFile", required = false) MultipartFile file,
                              RedirectAttributes redirectAttributes) {
        try {
            // A. Xử lý Upload ảnh
            if (file != null && !file.isEmpty()) {
                // 1. Tạo tên file mới
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

                // 2. [FIX CHUẨN] Dùng Paths.get để tự xử lý đường dẫn (uploads/product)
                // Nó sẽ tự tìm thư mục uploads ngay tại nơi file pom.xml nằm
                Path uploadPath = Paths.get("uploads", "product");

                // Kiểm tra và tạo thư mục nếu chưa có
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // 3. Lưu file vào đó
                Path filePath = uploadPath.resolve(fileName);
                try (var inputStream = file.getInputStream()) {
                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                }

                // 4. Lưu TÊN FILE vào DB
                nvkProduct.setNvkImgUrl(fileName);
            }

            // B. Lưu vào Database
            productService.saveProduct(nvkProduct);
            redirectAttributes.addFlashAttribute("nvkMsg", "Cập nhật thành công: " + nvkProduct.getNvkName());

        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("nvkMsg", "Lỗi upload ảnh: " + e.getMessage());
        }

        return "redirect:/nvkAdmin/product";
    }

    // 5. XÓA
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id,
                                RedirectAttributes redirectAttributes) {
        try {
            // Thử xóa bình thường
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("nvkMsg", "Đã xóa sản phẩm thành công!");
        } catch (Exception e) {
            // Nếu lỗi (do dính khóa ngoại foreign key), chuyển sang xóa mềm
            nvkProduct product = productService.getProductById(id);
            if (product != null) {
                product.setNvkStatus(0); // Chuyển trạng thái về 0: Ngừng bán
                productService.saveProduct(product); // Lưu lại

                redirectAttributes.addFlashAttribute("nvkMsg",
                        "Sản phẩm này đã có đơn hàng! Hệ thống đã chuyển sang trạng thái 'NGỪNG BÁN' thay vì xóa vĩnh viễn.");
            }
        }

        return "redirect:/nvkAdmin/product";
    }

    // 6. LIVE SEARCH
    @GetMapping("/search-results")
    public String searchResults(Model model,
                                @RequestParam(value = "keyword", required = false) String keyword,
                                @RequestParam(value = "categoryId", required = false) Long categoryId) {
        List<nvkProduct> list = productService.searchAndFilter(keyword, categoryId);
        model.addAttribute("nvkProducts", list);
        return "admin/product/list :: product_rows";
    }
}