package com.K23CNT2.NVKProject3.nvkController;

import com.K23CNT2.NVKProject3.nvkEntity.nvkAdmin;
import com.K23CNT2.NVKProject3.nvkRepository.nvkAdminRepository;
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
@RequestMapping("/nvkAdmin/accounts")
public class nvkAdminManagerController {

    @Autowired
    private nvkAdminRepository adminRepository;

    // ... (Các hàm list, create, edit giữ nguyên) ...

    @GetMapping("")
    public String list(Model model) {
        model.addAttribute("nvkAdmins", adminRepository.findAll());
        return "admin/accounts/list";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("nvkAdmin", new nvkAdmin());
        return "admin/accounts/form";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("nvkAdmin", adminRepository.findById(id).orElse(null));
        return "admin/accounts/form";
    }

    // 👇 HÀM SAVE ĐÃ ĐƯỢC NÂNG CẤP 👇
    @PostMapping("/save")
    public String save(@ModelAttribute nvkAdmin nvkAdmin,
                       @RequestParam("nvkImageFile") MultipartFile file) { // 1. Thêm tham số nhận file

        // 2. Xử lý Upload file (Giống hệt bên Customer/Product)
        if (!file.isEmpty()) {
            try {
                // Tạo tên file mới: time_tengoc.jpg (để tránh trùng tên và cache)
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

                // Đường dẫn lưu ảnh
                Path uploadPath = Paths.get("src/main/resources/static/images/");

                // Nếu chưa có thư mục thì tạo
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Lưu file vào thư mục
                try (InputStream inputStream = file.getInputStream()) {
                    Files.copy(inputStream, uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                }

                // Cập nhật đường dẫn ảnh mới vào đối tượng Admin
                nvkAdmin.setNvkAvatar("/images/" + fileName);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Nếu không chọn ảnh mới, nó sẽ tự giữ nguyên ảnh cũ (nhờ input hidden bên form)

        // 3. Lưu vào Database
        adminRepository.save(nvkAdmin);

        return "redirect:/nvkAdmin/accounts";
    }

    // ... (Hàm delete giữ nguyên) ...
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        adminRepository.deleteById(id);
        return "redirect:/nvkAdmin/accounts";
    }
}