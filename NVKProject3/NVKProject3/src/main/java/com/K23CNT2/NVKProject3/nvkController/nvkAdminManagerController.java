package com.K23CNT2.NVKProject3.nvkController;

import com.K23CNT2.NVKProject3.nvkEntity.nvkAdmin;
import com.K23CNT2.NVKProject3.nvkRepository.nvkAdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/nvkAdmin/accounts")
public class nvkAdminManagerController {

    @Autowired
    private nvkAdminRepository adminRepository; // Gọi thẳng Repo cho nhanh

    // Danh sách
    @GetMapping("")
    public String list(Model model) {
        model.addAttribute("nvkAdmins", adminRepository.findAll());
        return "admin/accounts/list";
    }

    // Thêm mới
    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("nvkAdmin", new nvkAdmin());
        return "admin/accounts/form";
    }

    // Sửa
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("nvkAdmin", adminRepository.findById(id).orElse(null));
        return "admin/accounts/form";
    }

    // Lưu
    @PostMapping("/save")
    public String save(@ModelAttribute nvkAdmin nvkAdmin) {
        // Lưu ý: Mật khẩu nên mã hóa, nhưng bài này đang làm thô nên lưu thẳng
        adminRepository.save(nvkAdmin);
        return "redirect:/nvkAdmin/accounts";
    }

    // Xóa
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        adminRepository.deleteById(id);
        return "redirect:/nvkAdmin/accounts";
    }
}