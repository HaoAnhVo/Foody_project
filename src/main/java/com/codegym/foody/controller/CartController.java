package com.codegym.foody.controller;

import com.codegym.foody.model.Cart;
import com.codegym.foody.model.CartItem;
import com.codegym.foody.model.Menu;
import com.codegym.foody.model.User;
import com.codegym.foody.service.impl.CartService;
import com.codegym.foody.service.impl.MenuService;
import com.codegym.foody.service.impl.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private MenuService menuService;
    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;
    @GetMapping("/add/{id}")
    public String addMenuToCart(@PathVariable(value = "id") Long id, HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }
        Optional<User> userOptional = userService.findByUsername(username);
        if (userOptional.isEmpty()) {
            return "redirect:/login";
        }
        User loggedInUser = userOptional.get();
        Cart cart = cartService.findByUser(loggedInUser);
        List<CartItem> cartItems = cart.getCartItems();
        Menu menu = menuService.getById(id);
        boolean itemExists = false;
        for (CartItem cartItem:cartItems) {
            if (cartItem.getMenu().equals(menu.getId())){
                cartItem.setQuantity(cartItem.getQuantity()+1);
                itemExists = true;
                break;
            }
        }
        if (!itemExists){
            CartItem cartItem = new CartItem();
            cartItem.setMenu(menu);
            cartItem.setQuantity(1);
            cartItem.setCart(cart);
            cartItems.add(cartItem);
        }
        model.addAttribute("cartItems", cartItems);
        return "cart";
    }

    private BigDecimal getTotalAmount(List<CartItem> cartItems) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CartItem cartItem : cartItems) {
            BigDecimal price = cartItem.getMenu().getPrice();
            Integer quantity = cartItem.getQuantity();
            BigDecimal itemTotal = price.multiply(BigDecimal.valueOf(quantity));
            totalAmount = totalAmount.add(itemTotal);
        }
        return totalAmount;
    }

}
