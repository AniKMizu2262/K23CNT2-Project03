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

    // --- LIST ---
    @GetMapping("")
    public String listProduct(Model model) {
        model.addAttribute("nvkProducts", productService.getAllProducts());
        return "admin/product/list";
    }

    // --- CREATE ---
    @GetMapping("/create")
    public String createProduct(Model model) {
        model.addAttribute("nvkProduct", new nvkProduct());
        model.addAttribute("nvkCategories", categoryService.getAllCategories());
        return "admin/product/form";
    }

    // --- EDIT ---
    @GetMapping("/edit/{id}")
    public String editProduct(@PathVariable("id") Long id, Model model) {
        model.addAttribute("nvkProduct", productService.getProductById(id));
        model.addAttribute("nvkCategories", categoryService.getAllCategories());
        return "admin/product/form";
    }

    // --- SAVE (Upload Logic) ---
    @PostMapping("/save")
    public String saveProduct(@ModelAttribute("nvkProduct") nvkProduct nvkProduct,
                              @RequestParam("nvkImageFile") MultipartFile file) {

        if (!file.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path uploadPath = Paths.get("src/main/resources/static/images/");

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                try (InputStream inputStream = file.getInputStream()) {
                    Files.copy(inputStream, uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                }

                nvkProduct.setNvkImgUrl("/images/" + fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        productService.saveProduct(nvkProduct);
        return "redirect:/nvkAdmin/product";
    }

    // --- DELETE ---
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return "redirect:/nvkAdmin/product";
    }
}