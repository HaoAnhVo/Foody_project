package com.codegym.foody.service;

import com.codegym.foody.model.Restaurant;
import com.codegym.foody.model.User;
import com.codegym.foody.model.enumable.ApprovalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IRestaurantService extends IGeneralService<Restaurant> {
    Page<Restaurant> findWithPaginationAndKeyword(String keyword, Pageable pageable);
    List<Restaurant> getAllRestaurants();
    List<Restaurant> findByUserId(Long userId);
    Page<Restaurant> findByUserId(Long userId, Pageable pageable);
    Page<Restaurant> findByUserIdAndKeyword(Long userId, String keyword, Pageable pageable);
    List<Restaurant> findByApprovalStatus(ApprovalStatus status);
    long countByApprovalStatus(ApprovalStatus status);
}
