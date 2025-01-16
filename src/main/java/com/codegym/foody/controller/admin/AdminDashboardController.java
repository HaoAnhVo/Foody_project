package com.codegym.foody.controller.admin;


import com.codegym.foody.model.Restaurant;
import com.codegym.foody.model.enumable.ApprovalStatus;
import com.codegym.foody.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/dashboard")
public class AdminDashboardController {
    @Autowired
    private UserService userService;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String dashboard(Model model) {
        // Tài khoản
        long totalUsers = userService.getTotal();
        // Nhà hàng
        long totalRestaurants = restaurantService.getTotal();
        // Nhà hàng đang chờ phê duyệt
        long totalPendingRestaurants = restaurantService.countByApprovalStatus(ApprovalStatus.PENDING);
        // Món ăn
        long totalMenus = menuService.getTotal();
        // Danh mục món ăn
        long totalCategories = categoryService.getTotal();

        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalRestaurants", totalRestaurants);
        model.addAttribute("totalPendingRestaurants", totalPendingRestaurants);
        model.addAttribute("totalMenus", totalMenus);
        model.addAttribute("totalCategories", totalCategories);
        model.addAttribute("page", "dashboard");
        return "admin/dashboard";
    }
}
