package com.K23CNT2.NVKProject3.nvkController;

import com.K23CNT2.NVKProject3.nvkEntity.nvkReview;
import com.K23CNT2.NVKProject3.nvkService.nvkReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/nvkAdmin/review") // URL chuẩn cho trang Admin
public class nvkReviewController {

    @Autowired
    private nvkReviewService reviewService; // Dùng Service mà mình vừa tạo lúc nãy

    // ==========================================================
    // 1. HIỂN THỊ DANH SÁCH + LỌC (Kết hợp Sao & Từ khóa)
    // ==========================================================
    @GetMapping("")
    public String listReview(Model model,
                             @RequestParam(value = "rating", required = false) Integer rating,
                             @RequestParam(value = "keyword", required = false) String keyword) {

        // Gọi hàm searchAndFilter bên Service
        List<nvkReview> list = reviewService.searchAndFilter(rating, keyword);
        model.addAttribute("nvkReviews", list);

        // Giữ lại giá trị filter để hiển thị trên giao diện
        model.addAttribute("currRating", rating);
        model.addAttribute("currKeyword", keyword);

        return "admin/review/list";
    }

    // ==========================================================
    // 2. API LIVE SEARCH (Trả về Fragment bảng)
    // ==========================================================
    @GetMapping("/search-results")
    public String searchResults(Model model,
                                @RequestParam(value = "rating", required = false) Integer rating,
                                @RequestParam(value = "keyword", required = false) String keyword) {

        List<nvkReview> list = reviewService.searchAndFilter(rating, keyword);
        model.addAttribute("nvkReviews", list);

        return "admin/review/list :: review_rows";
    }

    // ==========================================================
    // 3. XÓA ĐÁNH GIÁ (Admin chỉ có quyền xóa)
    // ==========================================================
    @GetMapping("/delete/{id}")
    public String deleteReview(@PathVariable("id") Long id,
                               RedirectAttributes redirectAttributes) {

        reviewService.deleteReview(id);

        redirectAttributes.addFlashAttribute("nvkMsg", "Đã xóa đánh giá thành công!");

        return "redirect:/nvkAdmin/review";
    }
}