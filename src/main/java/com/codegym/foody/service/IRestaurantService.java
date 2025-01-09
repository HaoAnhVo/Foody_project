package com.codegym.foody.service;

import com.codegym.foody.model.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IRestaurantService extends IGeneralService<Restaurant> {
    Page<Restaurant> findWithPaginationAndKeyword(String keyword, Pageable pageable);
    List<Restaurant> getAllRestaurants();
    List<Restaurant> findByUserId(Long userId);
}
