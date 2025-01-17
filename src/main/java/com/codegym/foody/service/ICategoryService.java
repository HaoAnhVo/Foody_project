package com.codegym.foody.service;

import com.codegym.foody.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICategoryService extends IGeneralService<Category> {
    Page<Category> findWithPaginationAndKeyword(String keyword, Pageable pageable);
    List<Category> getAllCategories();
}
