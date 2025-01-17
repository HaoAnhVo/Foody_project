package com.codegym.foody.controller.admin;

import com.codegym.foody.controller.common.BaseMenuController;
import com.codegym.foody.model.Category;
import com.codegym.foody.model.Menu;
import com.codegym.foody.model.Restaurant;
import com.codegym.foody.model.dto.PaginationResult;
import com.codegym.foody.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/menus")
public class MenuController extends BaseMenuController {
    @Autowired
    private MenuService menuService;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private PaginationService paginationService;

    @Override
    protected String getViewPrefix() {
        return "admin/menus";
    }

    @Override
    protected String getRedirectUrl() {
        return "redirect:/admin/menus";
    }

    @GetMapping
    public String listMenus(@RequestParam(name = "keyword", required = false) String keyword,
                            @RequestParam(name = "categoryId", required = false) Long categoryId,
                            @RequestParam(name = "restaurantId", required = false) Long restaurantId,
                            @RequestParam(name = "page", defaultValue = "0") int page,
                            @RequestParam(value = "size", defaultValue = "10") int size,
                            Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Menu> menuPage = menuService.findWithPaginationAndKeywordAndRestaurant(keyword, categoryId, restaurantId, pageable);

        List<Category> categories = categoryService.getAllCategories();
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();

        PaginationResult paginationResult = paginationService.calculatePagination(menuPage);

        model.addAttribute("menuPage", menuPage);
        model.addAttribute("menus", menuPage.getContent());
        model.addAttribute("currentPage", paginationResult.getCurrentPage());
        model.addAttribute("totalPages", paginationResult.getTotalPages());
        model.addAttribute("pageNumbers", paginationResult.getPageNumbers());
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("restaurantId", restaurantId);
        model.addAttribute("categories", categories);
        model.addAttribute("restaurants", restaurants);
        model.addAttribute("page", "menus");
        return getViewPrefix() + "/list";
    }

}

