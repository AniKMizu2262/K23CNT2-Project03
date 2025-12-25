package com.K23CNT2.NVKProject3.nvkController;

import com.K23CNT2.NVKProject3.nvkEntity.nvkProduct;
import com.K23CNT2.NVKProject3.nvkService.nvkCategoryService;
import com.K23CNT2.NVKProject3.nvkService.nvkProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
@RequestMapping("/nvkAdmin/product")
public class nvkProductController {

    @Autowired
    private nvkProductService productService;
    @Autowired
    private nvkCategoryService categoryService;

    // Danh sách
    @GetMapping("")
    public String listProduct(Model model,
                              @RequestParam(value = "keyword", required = false) String keyword,
                              @RequestParam(value = "categoryId", required = false) Long categoryId) {
        model.addAttribute("nvkProducts", productService.searchAndFilter(keyword, categoryId));
        model.addAttribute("nvkCategories", categoryService.getAllCategories());
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        return "admin/product/list";
    }

    // Form Thêm/Sửa
    @GetMapping("/create")
    public String createProduct(Model model) {
        model.addAttribute("nvkProduct", new nvkProduct());
        model.addAttribute("nvkCategories", categoryService.getAllCategories());
        return "admin/product/form";
    }

    @GetMapping("/edit/{id}")
    public String editProduct(@PathVariable("id") Long id, Model model) {
        nvkProduct product = productService.getProductById(id);
        if (product == null) return "redirect:/nvkAdmin/product";

        model.addAttribute("nvkProduct", product);
        model.addAttribute("nvkCategories", categoryService.getAllCategories());
        return "admin/product/form";
    }

    // Lưu Sản phẩm (Kèm ảnh)
    @PostMapping("/save")
    public String saveProduct(@ModelAttribute("nvkProduct") nvkProduct product,
                              @RequestParam(value = "nvkImageFile", required = false) MultipartFile file,
                              RedirectAttributes ra) {
        try {
            if (file != null && !file.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path uploadPath = Paths.get("uploads", "product"); // Tự tìm folder uploads/product

                if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

                Files.copy(file.getInputStream(), uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                product.setNvkImgUrl(fileName); // Chỉ lưu tên file
            }
            productService.saveProduct(product);
            ra.addFlashAttribute("nvkMsg", "Cập nhật thành công: " + product.getNvkName());
        } catch (Exception e) {
            ra.addFlashAttribute("nvkMsg", "Lỗi: " + e.getMessage());
        }
        return "redirect:/nvkAdmin/product";
    }

    // Xóa (Xóa mềm nếu có đơn hàng)
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id, RedirectAttributes ra) {
        try {
            productService.deleteProduct(id);
            ra.addFlashAttribute("nvkMsg", "Đã xóa sản phẩm thành công!");
        } catch (Exception e) {
            nvkProduct p = productService.getProductById(id);
            if (p != null) {
                p.setNvkStatus(0); // Chuyển trạng thái: Ngừng bán
                productService.saveProduct(p);
                ra.addFlashAttribute("nvkMsg", "Sản phẩm đã có đơn hàng -> Chuyển sang trạng thái 'NGỪNG BÁN'.");
            }
        }
        return "redirect:/nvkAdmin/product";
    }

    // Live Search API
    @GetMapping("/search-results")
    public String searchResults(Model model,
                                @RequestParam(value = "keyword", required = false) String keyword,
                                @RequestParam(value = "categoryId", required = false) Long categoryId) {
        model.addAttribute("nvkProducts", productService.searchAndFilter(keyword, categoryId));
        return "admin/product/list :: product_rows";
    }
}