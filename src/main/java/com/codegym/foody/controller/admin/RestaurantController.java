package com.codegym.foody.controller.admin;

import com.codegym.foody.model.Restaurant;
import com.codegym.foody.model.User;
import com.codegym.foody.service.impl.RestaurantService;
import com.codegym.foody.service.impl.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/admin/restaurants")
public class RestaurantController {
    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String listRestaurants(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(required = false) String keyword,
                                  Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Restaurant> restaurantPage = restaurantService.findWithPaginationAndKeyword(keyword, pageable);

        int totalPages = restaurantPage.getTotalPages();
        int currentPage = restaurantPage.getNumber();

        int start = Math.max(0, currentPage - 2);
        int end = Math.min(totalPages, currentPage + 3);
        List<Integer> pageNumbers = IntStream.range(start, end).boxed().toList();

        model.addAttribute("restaurantPage", restaurantPage);
        model.addAttribute("restaurants", restaurantPage.getContent());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", "restaurants");
        return "admin/restaurants/list";
    }

    @GetMapping("/view/{id}")
    public String viewRestaurant(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Restaurant restaurant = restaurantService.getById(id);
        if (restaurant == null) {
            redirectAttributes.addFlashAttribute("messageType", "error");
            redirectAttributes.addFlashAttribute("message", "Nhà hàng không tồn tại.");
            return "redirect:/admin/restaurants";
        }

        model.addAttribute("restaurant", restaurant);
        return "admin/restaurants/view";
    }

    @GetMapping("/create")
    public String createRestaurantForm(Model model) {
        model.addAttribute("restaurant", new Restaurant());
        model.addAttribute("merchants", userService.findMerchants());
        return "admin/restaurants/form";
    }

    @PostMapping("/create")
    public String saveRestaurant(@ModelAttribute @Valid Restaurant restaurant, BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("merchants", userService.findMerchants());
            return "admin/restaurants/form";
        }
        restaurantService.save(restaurant);

        long totalRestaurants = restaurantService.getTotal();
        int pageSize = 10;
        int lastPage = (int) Math.ceil((double) totalRestaurants / pageSize) - 1;

        redirectAttributes.addFlashAttribute("messageType", "success");
        redirectAttributes.addFlashAttribute("message", "Thêm nhà hàng mới thành công.");
        return "redirect:/admin/restaurants?page=" + lastPage;
    }

    @GetMapping("/edit/{id}")
    public String editRestaurantForm(@PathVariable("id") Long id, Model model) {
        Restaurant restaurant = restaurantService.getById(id);
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("merchants", userService.findMerchants());
        return "admin/restaurants/form";
    }

    @PostMapping("/edit")
    public String updateRestaurant(@Valid @ModelAttribute("restaurant") Restaurant restaurant, BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("merchants", userService.findMerchants());
            return "admin/restaurants/form";
        }
        restaurantService.update(restaurant);
        int pageContainingRestaurant = restaurantService.getPage(restaurant.getId());
        redirectAttributes.addFlashAttribute("messageType", "success");
        redirectAttributes.addFlashAttribute("message", "Cập nhật thông tin nhà hàng thành công.");
        return "redirect:/admin/restaurants?page=" + pageContainingRestaurant;
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
            redirectAttributes.addFlashAttribute("message", "Không thể xóa");
        }
        return "redirect:/admin/restaurants?page=" + currentPage;
    }
}
