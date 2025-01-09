package com.codegym.foody.service;

import com.codegym.foody.model.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IMenuService extends IGeneralService<Menu> {
    Page<Menu> findWithPaginationAndKeywordAndRestaurant(String keyword, Long categoryId, Long restaurantId, Pageable pageable);
}
