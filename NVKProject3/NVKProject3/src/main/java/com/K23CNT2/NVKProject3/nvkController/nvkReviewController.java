package com.K23CNT2.NVKProject3.nvkController;

import com.K23CNT2.NVKProject3.nvkService.nvkReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/nvkAdmin/review")
public class nvkReviewController {

    @Autowired
    private nvkReviewService reviewService;

    @GetMapping("")
    public String listReview(Model model,
                             @RequestParam(value = "rating", required = false) Integer rating,
                             @RequestParam(value = "keyword", required = false) String keyword) {
        model.addAttribute("nvkReviews", reviewService.searchAndFilter(rating, keyword));
        model.addAttribute("currRating", rating);
        model.addAttribute("currKeyword", keyword);
        return "admin/review/list";
    }

    @GetMapping("/search-results")
    public String searchResults(Model model,
                                @RequestParam(value = "rating", required = false) Integer rating,
                                @RequestParam(value = "keyword", required = false) String keyword) {
        model.addAttribute("nvkReviews", reviewService.searchAndFilter(rating, keyword));
        return "admin/review/list :: review_rows";
    }

    @GetMapping("/delete/{id}")
    public String deleteReview(@PathVariable("id") Long id, RedirectAttributes ra) {
        reviewService.deleteReview(id);
        ra.addFlashAttribute("nvkMsg", "Đã xóa đánh giá thành công!");
        return "redirect:/nvkAdmin/review";
    }
}