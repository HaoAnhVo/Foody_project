package com.codegym.foody.controller;

import com.codegym.foody.model.Cart;
import com.codegym.foody.model.User;
import com.codegym.foody.service.impl.CartService;
import com.codegym.foody.service.impl.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;

@Controller
@RequestMapping("/register")
public class RegistrationController {
    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping
    public String registerUser(@ModelAttribute @Valid User user,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "register";
        }

        if (userService.existsByEmail(user.getEmail())) {
            result.rejectValue("email", "error.user", "Email này đã tồn tại!");
            return "register";
        }

        if (userService.existsByUsername(user.getUsername())) {
            result.rejectValue("username", "error.user", "Tên đăng nhập này đã tồn tại!");
            return "register";
        }

        if (userService.existsByPhone(user.getPhone())) {
            result.rejectValue("phone", "error.user", "Số điện thoại này đã tồn tại!");
            return "register";
        }

        user.setPassword(userService.encodePassword(user.getPassword()));
        userService.save(user);

        // Giỏ hàng
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setCartItems(new ArrayList<>());
        cartService.save(cart);

        redirectAttributes.addFlashAttribute("message", "Đăng ký thành công! Hãy đăng nhập để tiếp tục.");
        return "redirect:/login";
    }
}
