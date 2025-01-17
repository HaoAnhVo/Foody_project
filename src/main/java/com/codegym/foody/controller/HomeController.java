package com.codegym.foody.controller;

import com.codegym.foody.model.Cart;
import com.codegym.foody.model.Menu;
import com.codegym.foody.model.User;
import com.codegym.foody.service.impl.CartItemService;
import com.codegym.foody.service.impl.CartService;
import com.codegym.foody.service.impl.MenuService;
import com.codegym.foody.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/home")
public class HomeController {
    @Autowired
    private MenuService menuService;
    @Autowired
    private CartService cartService;
    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private UserService userService;
    @GetMapping
    public String homePage(Model model, Principal principal) {
        String username = principal.getName();
        Optional<User> userOptional = userService.findByUsername(username);
        User loggedInUser = userOptional.get();
        Cart cart = cartService.findByUser(loggedInUser);
        List<Menu> menuList = menuService.findAll();
        model.addAttribute("menus", menuList);
        Long numProduct = cartItemService.getNumProduct(cart.getId(), loggedInUser.getId());
        model.addAttribute("numProduct", numProduct);
        return "home";
    }
}
