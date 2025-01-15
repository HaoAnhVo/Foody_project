package com.codegym.foody.controller;

import com.codegym.foody.model.Menu;
import com.codegym.foody.service.impl.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {
    @Autowired
    private MenuService menuService;
    @GetMapping
    public String homePage(Model model) {
        List<Menu> menuList = menuService.findAll();
        model.addAttribute("menus", menuList);
        return "home";
    }
}
