package com.codegym.foody.controller.admin;

import com.codegym.foody.model.Category;
import com.codegym.foody.model.Menu;
import com.codegym.foody.model.Restaurant;
import com.codegym.foody.service.impl.CategoryService;
import com.codegym.foody.service.impl.MenuService;
import com.codegym.foody.service.impl.RestaurantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/admin/menus")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String listMenus(@RequestParam(name = "keyword", required = false) String keyword,
                            @RequestParam(name = "categoryId", required = false) Long categoryId,
                            @RequestParam(name = "restaurantId", required = false) Long restaurantId,
                            @RequestParam(name = "page", defaultValue = "0") int page,
                            Model model) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Menu> menuPage = menuService.findWithPaginationAndKeywordAndRestaurant(keyword, categoryId, restaurantId, pageable);

        List<Category> categories = categoryService.getAllCategories();
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();

        int totalPages = menuPage.getTotalPages();
        int currentPage = menuPage.getNumber();

        int start = Math.max(0, currentPage - 2);
        int end = Math.min(totalPages, currentPage + 3);
        List<Integer> pageNumbers = IntStream.range(start, end).boxed().toList();

        model.addAttribute("menuPage", menuPage);
        model.addAttribute("menus", menuPage.getContent());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("restaurantId", restaurantId);
        model.addAttribute("categories", categories);
        model.addAttribute("restaurants", restaurants);
        model.addAttribute("page", "menus");
        return "admin/menus/list";
    }

    @GetMapping("/create")
    public String createMenuForm(Model model) {
        model.addAttribute("menu", new Menu());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("restaurants", restaurantService.getAllRestaurants());
        return "admin/menus/form";
    }

    @PostMapping("/create")
    public String saveMenu(@ModelAttribute @Valid Menu menu, BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("restaurants", restaurantService.getAllRestaurants());
            return "admin/menus/form";
        }
        menuService.save(menu);

        long totalMenus = menuService.getTotal();
        int pageSize = 10;
        int lastPage = (int) Math.ceil((double) totalMenus / pageSize) - 1;

        redirectAttributes.addFlashAttribute("messageType", "success");
        redirectAttributes.addFlashAttribute("message", "Thêm menu mới thành công.");
        return "redirect:/admin/menus?page=" + lastPage;
    }

    @GetMapping("/edit/{id}")
    public String editMenuForm(@PathVariable("id") Long id, Model model) {
        Menu menu = menuService.getById(id);
        model.addAttribute("menu", menu);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("restaurants", restaurantService.getAllRestaurants());
        return "admin/menus/form";
    }

    @PostMapping("/edit")
    public String updateMenu(@Valid @ModelAttribute("menu") Menu menu, BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("restaurants", restaurantService.getAllRestaurants());
            return "admin/menus/form";
        }
        menuService.update(menu);
        int pageContainingMenu = menuService.getPage(menu.getId());
        redirectAttributes.addFlashAttribute("messageType", "success");
        redirectAttributes.addFlashAttribute("message", "Cập nhật thông tin menu thành công.");
        return "redirect:/admin/menus?page=" + pageContainingMenu;
    }

    @GetMapping("/delete/{id}")
    public String deleteMenu(@PathVariable("id") Long id, RedirectAttributes redirectAttributes,
                             @RequestParam(value = "currentPage", defaultValue = "0") int currentPage) {
        try {
            menuService.delete(id);

            long totalMenus = menuService.getTotal();
            int pageSize = 10;
            int lastPage = (int) Math.ceil((double) totalMenus / pageSize) - 1;

            if (currentPage > lastPage) {
                currentPage = Math.max(0, lastPage);
            }

            redirectAttributes.addFlashAttribute("messageType", "success");
            redirectAttributes.addFlashAttribute("message", "Xóa menu thành công.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("messageType", "error");
            redirectAttributes.addFlashAttribute("message", "Không thể xóa menu này.");
        }
        return "redirect:/admin/menus?page=" + currentPage;
    }
}

