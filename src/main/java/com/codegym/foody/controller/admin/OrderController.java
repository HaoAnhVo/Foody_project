package com.codegym.foody.controller.admin;

import com.codegym.foody.model.Order;
import com.codegym.foody.model.enumable.OrderStatus;
import com.codegym.foody.model.Restaurant;
import com.codegym.foody.model.dto.PaginationResult;
import com.codegym.foody.service.impl.OrderService;
import com.codegym.foody.service.impl.PaginationService;
import com.codegym.foody.service.impl.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private PaginationService paginationService;

    @GetMapping
    public String listOrders(@RequestParam(value = "keyword", required = false) String keyword,
                             @RequestParam(value = "restaurantId", required = false) Long restaurantId,
                             @RequestParam(value = "page", defaultValue = "0") int page,
                             @RequestParam(value = "size", defaultValue = "10") int size,
                             Model model) {
        if (keyword != null && keyword.trim().isEmpty()) {
            keyword = null;
        }

        if (restaurantId != null && restaurantId == 0) {
            restaurantId = null;
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orderPage = orderService.searchOrders(keyword, restaurantId, pageable);
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();

        PaginationResult paginationResult = paginationService.calculatePagination(orderPage);

        model.addAttribute("orderPage", orderPage);
        model.addAttribute("orders", orderPage.getContent());
        model.addAttribute("currentPage", paginationResult.getCurrentPage());
        model.addAttribute("totalPages", paginationResult.getTotalPages());
        model.addAttribute("pageNumbers", paginationResult.getPageNumbers());
        model.addAttribute("restaurants", restaurants);
        model.addAttribute("keyword", keyword);
        model.addAttribute("restaurantId", restaurantId);
        model.addAttribute("page", "orders");
        return "admin/orders/list";
    }

    @GetMapping("/view/{id}")
    public String viewOrderDetail(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Order order = orderService.getOrderById(id);
        if (order == null) {
            redirectAttributes.addFlashAttribute("messageType", "error");
            redirectAttributes.addFlashAttribute("message", "Đơn hàng không tồn tại.");
            return "redirect:/admin/orders";
        }
        model.addAttribute("order", order);
        return "admin/orders/view";
    }

    @PostMapping("/{id}/update-status")
    public String updateOrderStatus(@PathVariable Long id,
                                    @RequestParam("status") OrderStatus status, RedirectAttributes redirectAttributes) {
        orderService.updateOrderStatus(id, status);
        redirectAttributes.addFlashAttribute("messageType", "success");
        redirectAttributes.addFlashAttribute("message", "Cập nhật đơn hàng thành công.");
        return "redirect:/admin/orders";
    }

    @PostMapping("/{id}/cancel")
    public String cancelOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        orderService.cancelOrder(id);
        redirectAttributes.addFlashAttribute("messageType", "success");
        redirectAttributes.addFlashAttribute("message", "Hủy đơn hàng thành công.");
        return "redirect:/admin/orders";
    }
}
