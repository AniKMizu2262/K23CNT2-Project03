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
    private nvkAdminRepository adminRepository;

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

    @PostMapping("/save")
    public String save(@ModelAttribute nvkAdmin nvkAdmin) {
        adminRepository.save(nvkAdmin);
        return "redirect:/nvkAdmin/accounts";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        adminRepository.deleteById(id);
        return "redirect:/nvkAdmin/accounts";
    }
}