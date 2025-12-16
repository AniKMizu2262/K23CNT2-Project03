package com.K23CNT2.NVKProject3.nvkController;

import com.K23CNT2.NVKProject3.nvkEntity.nvkCustomer;
import com.K23CNT2.NVKProject3.nvkEntity.nvkProduct;
import com.K23CNT2.NVKProject3.nvkEntity.nvkReview;
import com.K23CNT2.NVKProject3.nvkRepository.nvkProductRepository;
import com.K23CNT2.NVKProject3.nvkRepository.nvkReviewRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/nvk-review")
public class nvkReviewController {

    @Autowired
    private nvkReviewRepository nvkReviewRepo;

    @Autowired
    private nvkProductRepository nvkProductRepo;

    @PostMapping("/add")
    public String addReview(
            @RequestParam("productId") Long productId,
            @RequestParam("rating") Integer rating,
            @RequestParam("comment") String comment,
            HttpSession session) {

        // 1. Kiểm tra đăng nhập (Lấy user từ session)
        // Lưu ý: Key session phải trùng với lúc bro làm chức năng Login (ví dụ: "nvkUserLogin")
        nvkCustomer currentUser = (nvkCustomer) session.getAttribute("nvkUserLogin");

        if (currentUser == null) {
            return "redirect:/nvk-login"; // Chưa đăng nhập thì đá về trang login
        }

        // 2. Tìm sản phẩm cần đánh giá
        nvkProduct product = nvkProductRepo.findById(productId).orElse(null);

        if (product != null) {
            // 3. Tạo Review mới
            nvkReview review = nvkReview.builder()
                    .nvkRating(rating)
                    .nvkComment(comment)
                    .nvkProduct(product)
                    .nvkCustomer(currentUser)
                    .build();

            // 4. Lưu vào DB
            nvkReviewRepo.save(review);
        }

        // 5. Quay lại trang chi tiết sản phẩm
        return "redirect:/nvk-product/detail/" + productId;
    }
}