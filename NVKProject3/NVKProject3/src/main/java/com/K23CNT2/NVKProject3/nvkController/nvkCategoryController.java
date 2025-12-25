package com.K23CNT2.NVKProject3.nvkController;

import com.K23CNT2.NVKProject3.nvkEntity.nvkCategory;
import com.K23CNT2.NVKProject3.nvkService.nvkCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/nvkAdmin/category")
public class nvkCategoryController {

    @Autowired
    private nvkCategoryService categoryService;

    // Hiển thị danh sách
    @GetMapping("")
    public String listCategory(Model model, @RequestParam(value = "keyword", required = false) String keyword) {
        List<nvkCategory> list = (keyword != null && !keyword.isEmpty())
                ? categoryService.searchCategories(keyword.trim())
                : categoryService.getAllCategories();

        model.addAttribute("nvkCategories", list);
        model.addAttribute("keyword", keyword);
        return "admin/category/list";
    }

    // Form Thêm/Sửa
    @GetMapping("/create")
    public String createCategory(Model model) {
        model.addAttribute("nvkCategory", new nvkCategory());
        return "admin/category/form";
    }

    @GetMapping("/edit/{id}")
    public String editCategory(@PathVariable("id") Long id, Model model) {
        model.addAttribute("nvkCategory", categoryService.getCategoryById(id));
        return "admin/category/form";
    }

    // Lưu
    @PostMapping("/save")
    public String saveCategory(@ModelAttribute("nvkCategory") nvkCategory category, RedirectAttributes ra) {
        categoryService.saveCategory(category);
        ra.addFlashAttribute("nvkMsg", "Cập nhật thành công danh mục: " + category.getNvkName());
        return "redirect:/nvkAdmin/category";
    }

    // Xóa (Có check lỗi)
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id, RedirectAttributes ra) {
        try {
            categoryService.deleteCategory(id);
            ra.addFlashAttribute("nvkMsg", "Đã xóa danh mục thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("nvkMsg", "Không thể xóa! Danh mục này đang chứa sản phẩm.");
        }
        return "redirect:/nvkAdmin/category";
    }

    // Live Search API
    @GetMapping("/search-results")
    public String searchResults(Model model, @RequestParam("keyword") String keyword) {
        List<nvkCategory> list = (keyword != null && !keyword.isEmpty())
                ? categoryService.searchCategories(keyword.trim())
                : categoryService.getAllCategories();
        model.addAttribute("nvkCategories", list);
        return "admin/category/list :: category_rows";
    }
}