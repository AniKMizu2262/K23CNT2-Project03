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
    private nvkCategoryService categoryService; // <-- Tên biến là 'categoryService'

    // Admin xem danh sách quản lý
    @GetMapping("")
    public String listCategory(Model model,
                               @RequestParam(value = "keyword", required = false) String keyword) {

        List<nvkCategory> list;

        // KIỂM TRA: Nếu có từ khóa thì tìm, không thì lấy hết
        if (keyword != null && !keyword.isEmpty()) {
            // FIX 2: Sửa 'this.nvkCategoryService' thành 'categoryService' cho khớp dòng 17
            list = categoryService.searchCategories(keyword.trim());
        } else {
            // FIX 3: Tương tự, dùng đúng tên biến 'categoryService'
            list = categoryService.getAllCategories();
        }

        model.addAttribute("nvkCategories", list);
        model.addAttribute("keyword", keyword);

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
    public String saveCategory(@ModelAttribute("nvkCategory") nvkCategory nvkCategory,
                               RedirectAttributes redirectAttributes) { // <--- Thêm tham số này

        categoryService.saveCategory(nvkCategory);

        // Tạo thông báo
        redirectAttributes.addFlashAttribute("nvkMsg", "Cập nhật thành công danh mục: " + nvkCategory.getNvkName());

        return "redirect:/nvkAdmin/category";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id,
                                 RedirectAttributes redirectAttributes) { // <--- Thêm tham số này

        // Kiểm tra xem danh mục có sản phẩm không trước khi xóa (Optional - làm cho xịn)
        // Nếu không cần kiểm tra thì cứ xóa thẳng tay
        try {
            categoryService.deleteCategory(id);
            redirectAttributes.addFlashAttribute("nvkMsg", "Đã xóa danh mục thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("nvkMsg", "Không thể xóa danh mục này (có thể do đang chứa sản phẩm)!");
        }

        return "redirect:/nvkAdmin/category";
    }

    // --- [MỚI] API TRẢ VỀ FRAGMENT HTML CHO LIVE SEARCH ---
    @GetMapping("/search-results")
    public String searchResults(Model model, @RequestParam("keyword") String keyword) {
        List<nvkCategory> list;
        if (keyword != null && !keyword.isEmpty()) {
            list = categoryService.searchCategories(keyword.trim());
        } else {
            list = categoryService.getAllCategories();
        }
        model.addAttribute("nvkCategories", list);

        // QUAN TRỌNG: Chỉ trả về đoạn HTML của bảng (dựa vào th:fragment bên dưới)
        // Cú pháp: "tên-file-view :: tên-fragment"
        return "admin/category/list :: category_rows";
    }
}