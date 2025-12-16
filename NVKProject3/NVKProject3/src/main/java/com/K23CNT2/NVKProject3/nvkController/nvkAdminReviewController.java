package com.K23CNT2.NVKProject3.nvkController;

import com.K23CNT2.NVKProject3.nvkService.nvkReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/nvkAdmin/review")
public class nvkAdminReviewController {

    @Autowired
    private nvkReviewService nvkReviewService;

    // --- HIỂN THỊ DANH SÁCH ---
    @GetMapping("")
    public String listReviews(Model model) {
        model.addAttribute("nvkReviews", nvkReviewService.getAllReviews());
        return "admin/review/list"; // Trỏ đến file HTML bên dưới
    }

    // --- XÓA ĐÁNH GIÁ ---
    @GetMapping("/delete/{id}")
    public String deleteReview(@PathVariable("id") Long id) {
        nvkReviewService.deleteReview(id);
        return "redirect:/nvkAdmin/review";
    }
}