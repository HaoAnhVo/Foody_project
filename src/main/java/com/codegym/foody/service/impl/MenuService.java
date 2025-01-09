package com.codegym.foody.service.impl;

import com.codegym.foody.model.Menu;
import com.codegym.foody.repository.IMenuRepository;
import com.codegym.foody.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService implements IMenuService {
    @Autowired
    private IMenuRepository menuRepository;


    @Override
    public void save(Menu menu) {
        menuRepository.save(menu);
    }

    @Override
    public void update(Menu menu) {
        if (menuRepository.existsById(menu.getId())) {
            menuRepository.save(menu);
        } else {
            throw new RuntimeException("Không tìm thấy thực đơn");
        }
    }

    @Override
    public void delete(Long id) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Thực đơn không tồn tại"));
        menuRepository.delete(menu);
    }

    @Override
    public long getTotal() {
        return menuRepository.count();
    }

    @Override
    public Menu getById(Long id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thực đơn"));
    }

    @Override
    public int getPage(Long menuId) {
        int pageSize = 10;
        List<Menu> sortedMenus = menuRepository.findAll();
        int index = sortedMenus.indexOf(menuRepository.findById(menuId).orElse(null));
        return index / pageSize;
    }

    @Override
    public Page<Menu> findWithPaginationAndKeywordAndRestaurant(String keyword, Long categoryId, Long restaurantId, Pageable pageable) {
        return menuRepository.searchMenus(keyword, categoryId, restaurantId, pageable);
    }

}
