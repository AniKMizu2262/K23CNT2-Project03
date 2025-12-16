package com.K23CNT2.NVKProject3.nvkController;

import com.K23CNT2.NVKProject3.nvkEntity.nvkCategory;
import com.K23CNT2.NVKProject3.nvkService.nvkCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/nvkAdmin/category") // Prefix dành riêng cho Admin
public class nvkCategoryController {

    @Autowired
    private nvkCategoryService categoryService;

    // Admin xem danh sách quản lý
    @GetMapping("")
    public String listCategory(Model model) {
        model.addAttribute("nvkCategories", categoryService.getAllCategories());
        return "admin/category/list";
    }

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

    @PostMapping("/save")
    public String saveCategory(@ModelAttribute("nvkCategory") nvkCategory nvkCategory) {
        categoryService.saveCategory(nvkCategory);
        return "redirect:/nvkAdmin/category";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteCategory(id);
        return "redirect:/nvkAdmin/category";
    }
}