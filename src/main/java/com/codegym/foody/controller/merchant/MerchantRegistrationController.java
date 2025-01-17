package com.codegym.foody.controller.merchant;

import com.codegym.foody.model.enumable.Role;
import com.codegym.foody.model.User;
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

@Controller
@RequestMapping("/register-merchant")
public class MerchantRegistrationController {
    @Autowired
    private UserService userService;

    @GetMapping
    public String showMerchantRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register-merchant";
    }

    @PostMapping
    public String registerMerchant(@ModelAttribute @Valid User user,
                                   BindingResult result,
                                   RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "register-merchant";
        }

        if (userService.existsByEmail(user.getEmail())) {
            result.rejectValue("email", "error.user", "Email này đã tồn tại!");
            return "register-merchant";
        }

        if (userService.existsByUsername(user.getUsername())) {
            result.rejectValue("username", "error.user", "Tên đăng nhập này đã tồn tại!");
            return "register-merchant";
        }

        if (userService.existsByPhone(user.getPhone())) {
            result.rejectValue("phone", "error.user", "Số điện thoại này đã tồn tại!");
            return "register-merchant";
        }

        user.setPassword(userService.encodePassword(user.getPassword()));
        user.setRole(Role.MERCHANT);
        userService.save(user);

        redirectAttributes.addFlashAttribute("message", "Đăng ký Merchant thành công! Hãy đăng nhập để tiếp tục.");
        return "redirect:/login";
    }
}
