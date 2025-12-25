package com.K23CNT2.NVKProject3.nvkService;

import com.K23CNT2.NVKProject3.nvkEntity.nvkReview;
import com.K23CNT2.NVKProject3.nvkRepository.nvkReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class nvkReviewService {

    @Autowired
    private nvkReviewRepository repo;

    public List<nvkReview> getAllReviews() {
        return repo.findAll();
    }

    public void deleteReview(Long id) {
        repo.deleteById(id);
    }

    /**
     * Lọc đánh giá theo Số sao và Từ khóa
     */
    public List<nvkReview> searchAndFilter(Integer rating, String keyword) {
        if (keyword != null && keyword.trim().isEmpty()) {
            keyword = null;
        }
        return repo.findByRatingAndKeyword(rating, keyword);
    }
}