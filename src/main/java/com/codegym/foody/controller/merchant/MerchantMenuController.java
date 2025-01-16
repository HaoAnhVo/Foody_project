package com.codegym.foody.controller.merchant;

import com.codegym.foody.controller.common.BaseMenuController;
import com.codegym.foody.model.*;
import com.codegym.foody.model.dto.PaginationResult;
import com.codegym.foody.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/merchant/menus")
public class MerchantMenuController extends BaseMenuController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private PaginationService paginationService;

    @Override
    protected String getViewPrefix() {
        return "merchant/menus";
    }

    @Override
    protected String getRedirectUrl() {
        return "redirect:/merchant/menus";
    }

    @GetMapping
    public String listMenus(@RequestParam(name = "keyword", required = false) String keyword,
                            @RequestParam(name = "restaurantId", required = false) Long restaurantId,
                            @RequestParam(name = "page", defaultValue = "0") int page,
                            @RequestParam(value = "size", defaultValue = "10") int size,
                            Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User currentUser = userService.findByUsername(username).orElse(null);

        if (currentUser == null) {
            return "error/403";
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Menu> menuPage = menuService.findAllMenusByMerchantId(currentUser.getId(), keyword, restaurantId, pageable);
        Page<Restaurant> restaurantPage = restaurantService.findByUserId(currentUser.getId(), pageable);
        List<Category> categories = categoryService.getAllCategories();

        PaginationResult paginationResult = paginationService.calculatePagination(menuPage);

        model.addAttribute("menuPage", menuPage);
        model.addAttribute("menus", menuPage.getContent());
        model.addAttribute("currentPage", paginationResult.getCurrentPage());
        model.addAttribute("totalPages", paginationResult.getTotalPages());
        model.addAttribute("pageNumbers", paginationResult.getPageNumbers());
        model.addAttribute("keyword", keyword);
        model.addAttribute("restaurantId", restaurantId);
        model.addAttribute("restaurants", restaurantPage.getContent());
        model.addAttribute("categories", categories);
        model.addAttribute("page", "menus");

        return getViewPrefix() + "/list";
    }

}


