package com.K23CNT2.NVKProject3.nvkService;

import com.K23CNT2.NVKProject3.nvkEntity.nvkReview;
import com.K23CNT2.NVKProject3.nvkRepository.nvkReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class nvkReviewService {

    @Autowired
    private nvkReviewRepository nvkReviewRepository;

    // 1. Lấy tất cả đánh giá
    public List<nvkReview> getAllReviews() {
        return nvkReviewRepository.findAll();
    }

    // 2. Xóa đánh giá theo ID
    public void deleteReview(Long id) {
        nvkReviewRepository.deleteById(id);
    }

    // Lấy danh sách có lọc
    public List<nvkReview> searchAndFilter(Integer rating, String keyword) {
        if (keyword != null && keyword.trim().isEmpty()) keyword = null;
        return nvkReviewRepository.findByRatingAndKeyword(rating, keyword);
    }
}