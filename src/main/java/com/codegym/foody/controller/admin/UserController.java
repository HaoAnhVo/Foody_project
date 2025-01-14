package com.codegym.foody.controller.admin;

import com.codegym.foody.model.Cart;
import com.codegym.foody.model.Restaurant;
import com.codegym.foody.model.enumable.Role;
import com.codegym.foody.model.User;
import com.codegym.foody.model.dto.PaginationResult;
import com.codegym.foody.service.impl.CartService;
import com.codegym.foody.service.impl.PaginationService;
import com.codegym.foody.service.impl.RestaurantService;
import com.codegym.foody.service.impl.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Controller
@RequestMapping("/admin/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private PaginationService paginationService;

    @Autowired
    private CartService cartService;

    @GetMapping
    public String listUsers(@RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size,
                            @RequestParam(required = false) String keyword,
                            @RequestParam(required = false) String role,
                            Model model) {
        List<Role> allRoles  = Arrays.asList(Role.MERCHANT, Role.CUSTOMER);
        List<Role> rolesToFilter = new ArrayList<>(allRoles);

        if (role != null && !role.isEmpty()) {
            try {
                Role selectedRole = Role.valueOf(role);
                if (allRoles.contains(selectedRole)) {
                    rolesToFilter = List.of(selectedRole);
                }
            } catch (IllegalArgumentException ignored) {
            }
        }

        Page<User> userPage = userService.findUsersWithPaginationAndRoleFilter(
                rolesToFilter, page, size, keyword);

        PaginationResult paginationResult = paginationService.calculatePagination(userPage);

        model.addAttribute("userPage", userPage);
        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", paginationResult.getCurrentPage());
        model.addAttribute("totalPages", paginationResult.getTotalPages());
        model.addAttribute("pageNumbers", paginationResult.getPageNumbers());
        model.addAttribute("keyword", keyword);
        model.addAttribute("role", role);
        model.addAttribute("allRoles", allRoles);
        model.addAttribute("page", "users");
        return "admin/users/list";
    }

    @GetMapping("/view/{id}")
    public String viewUser(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        User user = userService.getById(id);
        if (user == null) {
            redirectAttributes.addFlashAttribute("messageType", "error");
            redirectAttributes.addFlashAttribute("message", "Người dùng không tồn tại.");
            return "redirect:/admin/users";
        }

        if (user.getRole() == Role.MERCHANT) {
            List<Restaurant> restaurants = restaurantService.findByUserId(id);
            model.addAttribute("restaurants", restaurants);
        }

        model.addAttribute("user", user);
        return "admin/users/view";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", Role.values());
        return "admin/users/form";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute @Valid User user, BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("roles", Role.values());
            return "admin/users/form";
        }

        if (userService.existsByEmail(user.getEmail())) {
            result.rejectValue("email", "error.user", "Email này đã tồn tại!");
            return "admin/users/form";
        }

        if (userService.existsByUsername(user.getUsername())) {
            result.rejectValue("username", "error.user", "Tên đăng nhập này đã tồn tại!");
            return "admin/users/form";
        }

        if (userService.existsByPhone(user.getPhone())) {
            result.rejectValue("phone", "error.user", "Số điện thoại này đã tồn tại!");
            return "admin/users/form";
        }

        user.setPassword(userService.encodePassword(user.getPassword()));
        userService.save(user);

        // Giỏ hàng
        if (user.getRole() == Role.CUSTOMER) {
            Cart cart = new Cart();
            cart.setUser(user);
            cart.setCartItems(new ArrayList<>());
            cartService.save(cart);
        }

        // Xử lý chuyển trang cuối cùng khi thêm mới user thành công
        long totalUsers = userService.getTotal();
        int pageSize = 10;
        int lastPage = (int) Math.ceil((double) totalUsers / pageSize) - 1;

        redirectAttributes.addFlashAttribute("messageType", "success");
        redirectAttributes.addFlashAttribute("message", "Thêm người dùng mới thành công.");
        return "redirect:/admin/users?page=" + lastPage;
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        User user = userService.getById(id);
        if (user == null) {
            redirectAttributes.addFlashAttribute("messageType", "error");
            redirectAttributes.addFlashAttribute("message", "Người dùng không tồn tại.");
            return "redirect:/admin/users";
        }

        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "admin/users/form";
    }

    @PostMapping("/edit")
    public String updateUser(@Valid @ModelAttribute("user") User user, BindingResult result, RedirectAttributes redirectAttributes) {
        User existingUser = userService.getById(user.getId());

        if (!existingUser.getEmail().equals(user.getEmail()) && userService.existsByEmail(user.getEmail())) {
            result.rejectValue("email", "error.user", "Email này đã tồn tại!");
            return "admin/users/form";
        }

        if (!existingUser.getUsername().equals(user.getUsername()) && userService.existsByUsername(user.getUsername())) {
            result.rejectValue("username", "error.user", "Tên người dùng này đã tồn tại!");
            return "admin/users/form";
        }

        if (!existingUser.getPhone().equals(user.getPhone()) && userService.existsByPhone(user.getPhone())) {
            result.rejectValue("phone", "error.user", "Số điện thoại này đã tồn tại!");
            return "admin/users/form";
        }

        existingUser.setFullname(user.getFullname());
        existingUser.setEmail(user.getEmail());
        existingUser.setPhone(user.getPhone());
        existingUser.setAddress(user.getAddress());
        userService.update(existingUser);

        if (!existingUser.getStatus().equals(user.getStatus())) {
            userService.updateStatusAndRelatedEntities(existingUser.getId(), user.getStatus());
        } else {
            userService.update(existingUser);
        }

        int pageContainingUser = userService.getPage(user.getId());

        redirectAttributes.addFlashAttribute("messageType", "success");
        redirectAttributes.addFlashAttribute("message", "Cập nhật thông tin người dùng thành công.");
        return "redirect:/admin/users?page=" + pageContainingUser;
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes,
                             @RequestParam(value = "currentPage", defaultValue = "0") int currentPage) {
        try {
            userService.delete(id);

            long totalUsers = userService.getTotal();
            int pageSize = 10;
            int lastPage = (int) Math.ceil((double) totalUsers / pageSize) - 1;

            if (currentPage > lastPage) {
                currentPage = lastPage;
            }

            redirectAttributes.addFlashAttribute("messageType", "success");
            redirectAttributes.addFlashAttribute("message", "Xóa người dùng thành công.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("messageType", "error");
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/admin/users?page=" + currentPage;
    }
}