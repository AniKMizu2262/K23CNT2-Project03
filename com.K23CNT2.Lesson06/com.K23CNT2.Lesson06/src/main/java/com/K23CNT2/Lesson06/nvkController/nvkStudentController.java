package com.K23CNT2.Lesson06.nvkController;

import com.K23CNT2.Lesson06.nvkEntity.nvkStudent;
import com.K23CNT2.Lesson06.nvkService.nvkStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/nvkStudent")
public class nvkStudentController {

    @Autowired
    private nvkStudentService nvkStudentService;

    // 1. Hiển thị danh sách
    @GetMapping
    public String nvkList(Model model) {
        model.addAttribute("nvkListStudent", nvkStudentService.nvkGetAll());
        return "nvkStudent-list";
    }

    // 2. Form thêm mới
    @GetMapping("/create")
    public String nvkCreate(Model model) {
        model.addAttribute("nvkStudent", new nvkStudent());
        return "nvkStudent-create";
    }

    // 3. Lưu (Thêm/Sửa)
    @PostMapping("/save")
    public String nvkSave(@ModelAttribute("nvkStudent") nvkStudent nvkStudent) {
        nvkStudentService.nvkSave(nvkStudent);
        return "redirect:/nvkStudent";
    }

    // 4. Sửa
    @GetMapping("/edit/{id}")
    public String nvkEdit(@PathVariable("id") Long id, Model model) {
        nvkStudent student = nvkStudentService.nvkGetById(id).orElse(null);
        model.addAttribute("nvkStudent", student);
        return "nvkStudent-create";
    }

    // 5. Xóa
    @GetMapping("/delete/{id}")
    public String nvkDelete(@PathVariable("id") Long id) {
        nvkStudentService.nvkDelete(id);
        return "redirect:/nvkStudent";
    }
}
