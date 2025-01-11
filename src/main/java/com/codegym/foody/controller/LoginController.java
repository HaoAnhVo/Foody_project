package com.codegym.foody.controller;

import com.codegym.foody.model.Role;
import com.codegym.foody.model.User;
import com.codegym.foody.repository.IUserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class LoginController {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/home")
    public String userHome() {
        return "home";
    }
    @GetMapping("/register")
    public String showRegisterCustomerForm(Model model) {
        model.addAttribute("user", new User());
        List<Role> roles = Arrays.stream(Role.values())
                .filter(role -> role != Role.ADMIN)
                .filter(role -> role != Role.MERCHANT)
                .collect(Collectors.toList());
        model.addAttribute("roles", roles);
        return "register";
    }

    @PostMapping("/register")
    public String registerCustomer(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<Role> roles = Arrays.stream(Role.values())
                    .filter(role -> role != Role.ADMIN)
                    .collect(Collectors.toList());
            model.addAttribute("roles", roles);
            return "register";
        }

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            model.addAttribute("usernameError", "Username đã tồn tại!");
            List<Role> roles = Arrays.stream(Role.values())
                    .filter(role -> role != Role.ADMIN)
                    .collect(Collectors.toList());
            model.addAttribute("roles", roles);
            return "register";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/login";
    }
}