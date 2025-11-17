package com.K23CNT2.Lesson07.nvkController;

import com.K23CNT2.Lesson07.nvkEntity.nvkCategory;
import com.K23CNT2.Lesson07.nvkEntity.nvkProduct;
import com.K23CNT2.Lesson07.nvkService.nvkCategoryService;
import com.K23CNT2.Lesson07.nvkService.nvkProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class nvkMainController { // Tên class này phải TRÙNG với tên file nvkMainController

    @Autowired
    private nvkCategoryService nvkCategoryService;

    @Autowired
    private nvkProductService nvkProductService;

    // ================== QUẢN LÝ CATEGORY ==================
    @GetMapping("/nvkCategory")
    public String nvkListCat(Model model) {
        model.addAttribute("nvkListCategory", nvkCategoryService.nvkGetAllCategories());
        model.addAttribute("nvkCategory", new nvkCategory());
        // Trả về đúng tên file trong thư mục templates (bỏ đuôi .html)
        return "nvkCategory-view";
    }

    @PostMapping("/nvkCategory/save")
    public String nvkSaveCat(@ModelAttribute("nvkCategory") nvkCategory nvkCategory) {
        nvkCategoryService.nvkSaveCategory(nvkCategory);
        return "redirect:/nvkCategory";
    }

    // ================== QUẢN LÝ PRODUCT ==================
    @GetMapping("/nvkProduct")
    public String nvkListProd(Model model) {
        model.addAttribute("nvkListProduct", nvkProductService.nvkGetAllProducts());
        model.addAttribute("nvkProduct", new nvkProduct());
        model.addAttribute("nvkListCategory", nvkCategoryService.nvkGetAllCategories());
        // Trả về đúng tên file trong thư mục templates
        return "nvkProduct-view";
    }

    @PostMapping("/nvkProduct/save")
    public String nvkSaveProd(@ModelAttribute("nvkProduct") nvkProduct nvkProduct) {
        nvkProductService.nvkSaveProduct(nvkProduct);
        return "redirect:/nvkProduct";
    }

    // ================== CHỨC NĂNG CHO CATEGORY ==================

    // 1. Xóa Category
    @GetMapping("/nvkCategory/delete/{id}")
    public String nvkDeleteCat(@PathVariable("id") Long id) {
        nvkCategoryService.nvkDeleteCategory(id);
        return "redirect:/nvkCategory";
    }

    // 2. Sửa Category (Lấy dữ liệu lên form)
    @GetMapping("/nvkCategory/edit/{id}")
    public String nvkEditCat(@PathVariable("id") Long id, Model model) {
        // Lấy category theo id
        var categoryEdit = nvkCategoryService.nvkGetCategoryById(id).orElse(null);

        // Đẩy đối tượng vừa tìm được vào model để form nó hứng dữ liệu
        model.addAttribute("nvkCategory", categoryEdit);

        // Vẫn phải lấy danh sách để hiển thị bảng bên dưới
        model.addAttribute("nvkListCategory", nvkCategoryService.nvkGetAllCategories());

        return "nvkCategory-view";
    }

    // ================== CHỨC NĂNG CHO PRODUCT ==================

    // 3. Xóa Product
    @GetMapping("/nvkProduct/delete/{id}")
    public String nvkDeleteProd(@PathVariable("id") Long id) {
        nvkProductService.nvkDeleteProduct(id);
        return "redirect:/nvkProduct";
    }

    // 4. Sửa Product
    @GetMapping("/nvkProduct/edit/{id}")
    public String nvkEditProd(@PathVariable("id") Long id, Model model) {
        var productEdit = nvkProductService.nvkGetProductById(id).orElse(null);

        model.addAttribute("nvkProduct", productEdit);

        // Cần list product để hiện bảng và list category để hiện dropdown
        model.addAttribute("nvkListProduct", nvkProductService.nvkGetAllProducts());
        model.addAttribute("nvkListCategory", nvkCategoryService.nvkGetAllCategories());

        return "nvkProduct-view";
    }
}