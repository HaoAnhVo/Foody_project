package com.codegym.foody.controller.common;

import com.codegym.foody.model.Menu;
import com.codegym.foody.model.Review;
import com.codegym.foody.service.impl.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public abstract class BaseMenuController {
    @Autowired
    protected MenuService menuService;

    @Autowired
    protected RestaurantService restaurantService;

    @Autowired
    protected CategoryService categoryService;

    @Autowired
    protected ReviewService reviewService;

    // Lấy ra đường dẫn
    protected abstract String getRedirectUrl();

    // Lấy ra view
    protected abstract String getViewPrefix();

    @GetMapping("/view/{id}")
    public String viewMenu(@PathVariable("id") Long id, Model model) {
        Menu menu = menuService.getById(id);
        List<Review> reviews = reviewService.findReviewsByMenuId(id);

        model.addAttribute("menu", menu);
        model.addAttribute("reviews", reviews);
        return getViewPrefix() + "/view";
    }

    @GetMapping("/create")
    public String createMenuForm(Model model) {
        model.addAttribute("menu", new Menu());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("restaurants", restaurantService.getAllRestaurants());
        return getViewPrefix() + "/form";
    }

    @PostMapping("/create")
    public String saveMenu(@ModelAttribute @Valid Menu menu, BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("restaurants", restaurantService.getAllRestaurants());
            return getViewPrefix() + "/form";
        }
        menuService.save(menu);

        long totalMenus = menuService.getTotal();
        int pageSize = 10;
        int lastPage = (int) Math.ceil((double) totalMenus / pageSize) - 1;

        redirectAttributes.addFlashAttribute("messageType", "success");
        redirectAttributes.addFlashAttribute("message", "Thêm menu mới thành công.");
        return getRedirectUrl() + "?page=" + lastPage;
    }

    @GetMapping("/edit/{id}")
    public String editMenuForm(@PathVariable("id") Long id, Model model) {
        Menu menu = menuService.getById(id);
        model.addAttribute("menu", menu);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("restaurants", restaurantService.getAllRestaurants());
        return getViewPrefix() + "/form";
    }

    @PostMapping("/edit")
    public String updateMenu(@Valid @ModelAttribute("menu") Menu menu, BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("restaurants", restaurantService.getAllRestaurants());
            return getViewPrefix() + "/form";
        }
        menuService.update(menu);
        int pageContainingMenu = menuService.getPage(menu.getId());
        redirectAttributes.addFlashAttribute("messageType", "success");
        redirectAttributes.addFlashAttribute("message", "Cập nhật thông tin menu thành công.");
        return getRedirectUrl() + "?page=" + pageContainingMenu;
    }

    @GetMapping("/delete/{id}")
    public String deleteMenu(@PathVariable("id") Long id, RedirectAttributes redirectAttributes,
                             @RequestParam(value = "currentPage", defaultValue = "0") int currentPage) {
        try {
            menuService.delete(id);

            long totalMenus = menuService.getTotal();
            int pageSize = 10;
            int lastPage = (int) Math.ceil((double) totalMenus / pageSize) - 1;

            if (currentPage > lastPage) {
                currentPage = Math.max(0, lastPage);
            }

            redirectAttributes.addFlashAttribute("messageType", "success");
            redirectAttributes.addFlashAttribute("message", "Xóa menu thành công.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("messageType", "error");
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return getRedirectUrl() + "?page=" + currentPage;
    }
}
