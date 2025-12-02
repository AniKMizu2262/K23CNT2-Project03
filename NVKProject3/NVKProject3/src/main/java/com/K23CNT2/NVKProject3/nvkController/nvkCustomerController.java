package com.K23CNT2.NVKProject3.nvkController;

import com.K23CNT2.NVKProject3.nvkEntity.nvkCustomer;
import com.K23CNT2.NVKProject3.nvkRepository.nvkCustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.*;
import java.util.List;

@Controller
@RequestMapping("/nvkAdmin/customer")
public class nvkCustomerController {

    @Autowired
    private nvkCustomerRepository customerRepository; // Gọi thẳng Repo cho nhanh

    // 1. Hiển thị danh sách Khách hàng
    @GetMapping("")
    public String listCustomer(Model model) {
        List<nvkCustomer> customers = customerRepository.findAll();
        model.addAttribute("nvkCustomers", customers);
        return "admin/customer/list";
    }

    // 2. Form Thêm mới Khách hàng
    @GetMapping("/create")
    public String createCustomer(Model model) {
        model.addAttribute("nvkCustomer", new nvkCustomer());
        return "admin/customer/form";
    }

    // 3. Xử lý Lưu Khách hàng (Thêm hoặc Sửa) + Upload Avatar
    @PostMapping("/save")
    public String saveCustomer(@ModelAttribute("nvkCustomer") nvkCustomer nvkCustomer,
                               @RequestParam("nvkImageFile") MultipartFile file) {

        // --- XỬ LÝ UPLOAD ẢNH AVATAR ---
        if (!file.isEmpty()) {
            try {
                // Tạo tên file ngẫu nhiên: time_tenfile.jpg
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

                // Đường dẫn lưu: src/main/resources/static/images/
                Path uploadPath = Paths.get("src/main/resources/static/images/");

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                try (InputStream inputStream = file.getInputStream()) {
                    Path filePath = uploadPath.resolve(fileName);
                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                }

                // Lưu đường dẫn vào DB
                nvkCustomer.setNvkAvatar("/images/" + fileName);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // --------------------------------

        // Nếu không chọn ảnh mới khi sửa, giữ nguyên ảnh cũ (đã có trong hidden field ở form)
        // Lưu vào DB
        customerRepository.save(nvkCustomer);

        return "redirect:/nvkAdmin/customer";
    }

    // 4. Form Sửa Khách hàng
    @GetMapping("/edit/{id}")
    public String editCustomer(@PathVariable("id") Long id, Model model) {
        nvkCustomer customer = customerRepository.findById(id).orElse(null);
        if (customer != null) {
            model.addAttribute("nvkCustomer", customer);
            return "admin/customer/form";
        }
        return "redirect:/nvkAdmin/customer";
    }

    // 5. Xóa Khách hàng
    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable("id") Long id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
        }
        return "redirect:/nvkAdmin/customer";
    }
}