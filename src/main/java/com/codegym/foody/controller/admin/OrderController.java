package com.codegym.foody.controller.admin;

import com.codegym.foody.model.Order;
import com.codegym.foody.model.OrderStatus;
import com.codegym.foody.model.Restaurant;
import com.codegym.foody.service.impl.OrderService;
import com.codegym.foody.service.impl.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/admin/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private RestaurantService restaurantService;

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

        int totalPages = orderPage.getTotalPages();
        int currentPage = orderPage.getNumber();

        int start = Math.max(0, currentPage - 2);
        int end = Math.min(totalPages, currentPage + 3);
        List<Integer> pageNumbers = IntStream.range(start, end).boxed().toList();

        model.addAttribute("orderPage", orderPage);
        model.addAttribute("orders", orderPage.getContent());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageNumbers", pageNumbers);
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
