package com.codegym.foody.controller;

import com.codegym.foody.model.Cart;
import com.codegym.foody.model.CartItem;
import com.codegym.foody.model.Menu;
import com.codegym.foody.model.User;
import com.codegym.foody.service.impl.CartItemService;
import com.codegym.foody.service.impl.CartService;
import com.codegym.foody.service.impl.MenuService;
import com.codegym.foody.service.impl.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private MenuService menuService;
    @Autowired
    private CartService cartService;
    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private UserService userService;
    @GetMapping("/add/{id}")
    public String addMenuToCart(@PathVariable(value = "id") Long id, RedirectAttributes redirectAttributes, Model model, Principal principal) {
        String username = principal.getName();
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
            if (cartItem.getMenu().getId().equals(menu.getId())){
                cartItem.setQuantity(cartItem.getQuantity()+1);
                cartItemService.save(cartItem);
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
            cartItemService.save(cartItem);
        }

        redirectAttributes.addFlashAttribute("messageType", "success");
        redirectAttributes.addFlashAttribute("message", "Cập nhật giỏ hàng thành công.");
        return "redirect:/home";
    }

    @GetMapping()
    public String getCartDetail(Model model, Principal principal) {
        String username = principal.getName();
        Optional<User> userOptional = userService.findByUsername(username);
        User user = userOptional.get();
        Cart cart = cartService.findByUser(user);
        List<CartItem> cartItems = cart.getCartItems();

        BigDecimal totalAmount = getTotalAmount(cartItems);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("cartItems", cartItems);
        Long numProduct = cartItemService.getNumProduct(cart.getId(),user.getId());
        model.addAttribute("numProduct", numProduct);
        return "cart";
    }

    @GetMapping("/update")
    public String updateCartForm(Principal principal, Model model) {
        String username = principal.getName();
        Optional<User> userOptional = userService.findByUsername(username);
        User user = userOptional.get();
        Cart cart = cartService.findByUser(user);
        List<CartItem> cartItems = cart.getCartItems();
        model.addAttribute("cartItems", cartItems);
        BigDecimal totalAmount = getTotalAmount(cartItems);
        model.addAttribute("totalAmount", totalAmount);
        return "cart-update";
    }

    @PostMapping("/update")
    public String updateCart(RedirectAttributes redirectAttributes, HttpServletRequest request, Principal principal) {
        String username = principal.getName();
        Optional<User> userOptional = userService.findByUsername(username);
        User user = userOptional.get();
        Cart cart = cartService.findByUser(user);
        List<CartItem> cartItems = cart.getCartItems();

        String[] quantities = request.getParameterValues("qty");
        if (quantities != null) {
            Iterator<CartItem> iterator = cartItems.iterator();
            int index = 0;
            while (iterator.hasNext()) {
                CartItem cartItem = iterator.next();
                int quantity = Integer.parseInt(quantities[index++]);
                if (quantity == 0) {
                    cartItemService.delete(cartItem.getId());
                    iterator.remove();
                } else {
                    cartItem.setQuantity(quantity);
                    cartItemService.save(cartItem);
                }
            }
        }

        cartService.save(cart);

        redirectAttributes.addFlashAttribute("messageType", "success");
        redirectAttributes.addFlashAttribute("message", "Cập nhật giỏ hàng thành công.");
        return "redirect:/cart/update";
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String deleteCartItem(@RequestParam Long id, RedirectAttributes redirectAttributes, Principal principal) {
        cartItemService.delete(id);
        redirectAttributes.addFlashAttribute("messageType", "success");
        redirectAttributes.addFlashAttribute("message", "Cập nhật giỏ hàng thành công.");
        return "redirect:/cart";
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
