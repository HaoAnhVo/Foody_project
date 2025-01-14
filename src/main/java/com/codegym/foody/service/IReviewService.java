package com.codegym.foody.service;

import com.codegym.foody.model.Review;

import java.util.List;

public interface IReviewService {
    List<Review> findReviewsByMenuId(Long menuId);
}
