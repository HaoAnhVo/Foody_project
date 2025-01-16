package com.codegym.foody.service.impl;

import com.codegym.foody.model.Review;
import com.codegym.foody.repository.IReviewRepository;
import com.codegym.foody.service.IReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService implements IReviewService {
    @Autowired
    private IReviewRepository reviewRepository;

    @Override
    public List<Review> findReviewsByMenuId(Long menuId) {
        return reviewRepository.findByMenuId(menuId);
    }
}
