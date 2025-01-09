package com.codegym.foody.service.impl;

import com.codegym.foody.model.Category;
import com.codegym.foody.model.Restaurant;
import com.codegym.foody.repository.ICategoryRepository;
import com.codegym.foody.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService implements ICategoryService {
    @Autowired
    private ICategoryRepository categoryRepository;

    @Override
    public Page<Category> findWithPaginationAndKeyword(String keyword, Pageable pageable) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            return categoryRepository.findByNameContainingIgnoreCase(keyword, pageable);
        }
        return categoryRepository.findAll(pageable);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public void save(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public void update(Category category) {
        if (categoryRepository.existsById(category.getId())) {
            categoryRepository.save(category);
        } else {
            throw new RuntimeException("Không tìm thấy danh mục món ăn");
        }
    }

    @Override
    public void delete(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Danh mục không tồn tại"));
        categoryRepository.delete(category);
    }

    @Override
    public long getTotal() {
        return categoryRepository.count();
    }

    @Override
    public Category getById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục"));
    }

    @Override
    public int getPage(Long categoryId) {
        int pageSize = 10;
        List<Category> sortedCategories = categoryRepository.findAll();
        int index = sortedCategories.indexOf(categoryRepository.findById(categoryId).orElse(null));
        return index / pageSize;
    }
}
