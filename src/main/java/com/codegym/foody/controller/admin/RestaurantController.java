package com.codegym.foody.controller.admin;

import com.codegym.foody.controller.common.BaseRestaurantController;
import com.codegym.foody.model.Restaurant;
import com.codegym.foody.model.dto.PaginationResult;
import com.codegym.foody.model.enumable.ApprovalStatus;
import com.codegym.foody.service.impl.EmailService;
import com.codegym.foody.service.impl.PaginationService;
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

@Controller
@RequestMapping("/admin/restaurants")
public class RestaurantController extends BaseRestaurantController {
    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private UserService userService;

    @Autowired
    private PaginationService paginationService;

    @Autowired
    private EmailService emailService;

    @Override
    protected String getRedirectUrl() {
        return "redirect:/admin/restaurants";
    }

    @Override
    protected String getViewPrefix() {
        return "admin/restaurants";
    }

    @GetMapping
    public String listRestaurants(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(required = false) String keyword,
                                  Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Restaurant> restaurantPage = restaurantService.findWithPaginationAndKeyword(keyword, pageable);
        PaginationResult paginationResult = paginationService.calculatePagination(restaurantPage);

        model.addAttribute("restaurantPage", restaurantPage);
        model.addAttribute("restaurants", restaurantPage.getContent());
        model.addAttribute("currentPage",  paginationResult.getCurrentPage());
        model.addAttribute("totalPages", paginationResult.getTotalPages());
        model.addAttribute("pageNumbers", paginationResult.getPageNumbers());
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", "restaurants");
        return "admin/restaurants/list";
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

    @GetMapping("/pending-restaurant")
    public String listPendingRestaurants(Model model) {
        List<Restaurant> pendingRestaurants = restaurantService.findByApprovalStatus(ApprovalStatus.PENDING);
        model.addAttribute("restaurants", pendingRestaurants);
        return "admin/restaurants/pending";
    }

    @PostMapping("/approve-restaurant")
    public String approveRestaurant(@RequestParam Long id) {
        Restaurant restaurant = restaurantService.getById(id);
        if (restaurant != null && restaurant.getApprovalStatus() == ApprovalStatus.PENDING) {
            restaurant.setApprovalStatus(ApprovalStatus.APPROVED);
            restaurant.setStatus(true);
            restaurantService.save(restaurant);
            emailService.sendApprovalEmail(restaurant.getUser().getEmail(), "Nhà hàng của bạn đã được duyệt.");
        }
        return "redirect:/admin/restaurants/pending-restaurant";
    }

    @PostMapping("/reject-restaurant")
    public String rejectRestaurant(@RequestParam Long id) {
        Restaurant restaurant = restaurantService.getById(id);
        if (restaurant != null) {
            restaurant.setApprovalStatus(ApprovalStatus.REJECTED);
            restaurantService.save(restaurant);
            emailService.sendApprovalEmail(restaurant.getUser().getEmail(), "Nhà hàng của bạn đã bị từ chối.");
        }
        return "redirect:/admin/restaurants/pending-restaurant";
    }
}
