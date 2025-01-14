package com.codegym.foody.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
        if ("locked".equals(error)) {
            model.addAttribute("errorMessage", "Tài khoản của bạn đang bị khóa. Vui lòng liên hệ với quản trị viên.");
        } else if (error != null) {
            model.addAttribute("errorMessage", "Tên đăng nhập hoặc mật khẩu không chính xác.");
        }
        return "login";
    }
}
