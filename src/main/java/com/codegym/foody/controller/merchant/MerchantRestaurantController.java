package com.codegym.foody.controller.merchant;

import com.codegym.foody.controller.common.BaseRestaurantController;
import com.codegym.foody.model.Restaurant;
import com.codegym.foody.model.User;
import com.codegym.foody.model.dto.PaginationResult;
import com.codegym.foody.model.enumable.ApprovalStatus;
import com.codegym.foody.service.impl.PaginationService;
import com.codegym.foody.service.impl.RestaurantService;
import com.codegym.foody.service.impl.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/merchant/restaurants")
public class MerchantRestaurantController extends BaseRestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private UserService userService;

    @Autowired
    private PaginationService paginationService;


    @Override
    protected String getRedirectUrl() {
        return "redirect:/merchant/restaurants";
    }

    @Override
    protected String getViewPrefix() {
        return "merchant/restaurants";
    }

    @GetMapping
    public String listMerchantRestaurants(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size,
                                          @RequestParam(required = false) String keyword,
                                          Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User currentUser = userService.findByUsername(username).orElse(null);

        if (currentUser == null) {
            return "error/403";
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Restaurant> restaurantPage = restaurantService.findByUserIdAndKeyword(currentUser.getId(), keyword, pageable);
        PaginationResult paginationResult = paginationService.calculatePagination(restaurantPage);


        model.addAttribute("restaurantPage", restaurantPage);
        model.addAttribute("restaurants", restaurantPage.getContent());
        model.addAttribute("currentPage", paginationResult.getCurrentPage());
        model.addAttribute("totalPages", paginationResult.getTotalPages());
        model.addAttribute("pageNumbers", paginationResult.getPageNumbers());
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", "restaurants");
        return "merchant/restaurants/list";
    }

    @PostMapping("/create")
    public String saveMerchantRestaurant(@ModelAttribute @Valid Restaurant restaurant, BindingResult result,
                                         RedirectAttributes redirectAttributes, Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User currentUser = userService.findByUsername(username).orElse(null);

        if (currentUser == null) {
            return "error/403";
        }

        if (result.hasErrors()) {
            return "merchant/restaurants/form";
        }

        // Gắn nhà hàng với merchant hiện tại
        restaurant.setUser(currentUser);
        restaurant.setApprovalStatus(ApprovalStatus.PENDING);
        restaurant.setStatus(false);
        restaurantService.save(restaurant);
        return "notification/success-registration-restaurant";
    }

    @PostMapping("/edit")
    public String updateMerchantRestaurant(@Valid @ModelAttribute("restaurant") Restaurant restaurant, BindingResult result,
                                           RedirectAttributes redirectAttributes, Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User currentUser = userService.findByUsername(username).orElse(null);
        if (currentUser == null) {
            return "error/403";
        }

        if (result.hasErrors()) {
            return "merchant/restaurants/form";
        }

        restaurant.setUser(currentUser);
        restaurantService.update(restaurant);
        int pageContainingRestaurant = restaurantService.getPage(restaurant.getId());

        redirectAttributes.addFlashAttribute("messageType", "success");
        redirectAttributes.addFlashAttribute("message", "Cập nhật thông tin nhà hàng thành công.");
        return "redirect:/merchant/restaurants?page" + pageContainingRestaurant;
    }
}
