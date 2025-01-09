package com.codegym.foody.controller;

import com.codegym.foody.service.impl.CartItemService;
import com.codegym.foody.service.impl.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;
    private final CartItemService cartItemService;

    public CartController(CartService cartService, CartItemService cartItemService) {
        this.cartItemService = cartItemService;
        this.cartService = cartService;
    }
}
