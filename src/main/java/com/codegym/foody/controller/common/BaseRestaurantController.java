package com.codegym.foody.controller.common;

import com.codegym.foody.model.Restaurant;
import com.codegym.foody.service.impl.RestaurantService;
import com.codegym.foody.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public abstract class BaseRestaurantController {
    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private UserService userService;

    // Lấy ra đường dẫn
    protected abstract String getRedirectUrl();

    // Lấy ra view
    protected abstract String getViewPrefix();

    @GetMapping("/view/{id}")
    public String viewRestaurant(@PathVariable Long id, Model model) {
        Restaurant restaurant = restaurantService.getById(id);
        model.addAttribute("restaurant", restaurant);
        return getViewPrefix() + "/view";
    }

    @GetMapping("/create")
    public String createRestaurantForm(Model model) {
        model.addAttribute("restaurant", new Restaurant());
        model.addAttribute("merchants", userService.findMerchants());
        return getViewPrefix() + "/form";
    }

    @GetMapping("/edit/{id}")
    public String editRestaurantForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Restaurant restaurant = restaurantService.getById(id);
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("merchants", userService.findMerchants());
        return getViewPrefix() + "/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteRestaurant(@PathVariable("id") Long id, RedirectAttributes redirectAttributes,
                                           @RequestParam(value = "currentPage", defaultValue = "0") int currentPage) {
        try {
            restaurantService.delete(id);

            long totalRestaurants = restaurantService.getTotal();
            int pageSize = 10;
            int lastPage = (int) Math.ceil((double) totalRestaurants / pageSize) - 1;

            if (currentPage > lastPage) {
                currentPage = Math.max(0, lastPage);
            }

            redirectAttributes.addFlashAttribute("messageType", "success");
            redirectAttributes.addFlashAttribute("message", "Xóa nhà hàng thành công.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("messageType", "error");
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return getRedirectUrl() + "?page=" + currentPage;

    }
}
