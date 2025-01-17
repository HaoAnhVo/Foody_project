package com.codegym.foody.controller.admin;

import com.codegym.foody.model.Category;
import com.codegym.foody.model.dto.PaginationResult;
import com.codegym.foody.service.impl.CategoryService;
import com.codegym.foody.service.impl.PaginationService;
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


@Controller
@RequestMapping("/admin/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private PaginationService paginationService;

    @GetMapping
    public String listCategories(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Category> categoryPage = categoryService.findWithPaginationAndKeyword(keyword, pageable);

        PaginationResult paginationResult = paginationService.calculatePagination(categoryPage);

        model.addAttribute("categoryPage", categoryPage);
        model.addAttribute("categories", categoryPage.getContent());
        model.addAttribute("currentPage", paginationResult.getCurrentPage());
        model.addAttribute("totalPages", paginationResult.getTotalPages());
        model.addAttribute("pageNumbers", paginationResult.getPageNumbers());
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", "categories");
        return "admin/categories/list";
    }

    @GetMapping("/create")
    public String createCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "admin/categories/form";
    }

    @PostMapping("/create")
    public String createCategory(@Valid @ModelAttribute Category category, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/categories/form";
        }
        categoryService.save(category);

        long totalRestaurants = categoryService.getTotal();
        int pageSize = 10;
        int lastPage = (int) Math.ceil((double) totalRestaurants / pageSize) - 1;

        redirectAttributes.addFlashAttribute("messageType", "success");
        redirectAttributes.addFlashAttribute("message", "Thêm danh mục món ăn mới thành công.");
        return "redirect:/admin/categories?page=" + lastPage;
    }

    @GetMapping("/edit/{id}")
    public String editCategoryForm(@PathVariable Long id, Model model) {
        Category category = categoryService.getById(id);
        model.addAttribute("category", category);
        return "admin/categories/form";
    }

    @PostMapping("/edit")
    public String editCategory(@Valid @ModelAttribute("category") Category category, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/categories/form";
        }
        categoryService.update(category);
        int pageContainingCategory = categoryService.getPage(category.getId());
        redirectAttributes.addFlashAttribute("messageType", "success");
        redirectAttributes.addFlashAttribute("message", "Cập nhật thông tin danh mục thành công.");
        return "redirect:/admin/categories";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id, RedirectAttributes redirectAttributes,
                                 @RequestParam(value = "currentPage", defaultValue = "0") int currentPage) {
        try {
            categoryService.delete(id);

            long totalCategories = categoryService.getTotal();
            int pageSize = 10;
            int lastPage = (int) Math.ceil((double) totalCategories / pageSize) - 1;

            if (currentPage > lastPage) {
                currentPage = Math.max(0, lastPage);
            }

            redirectAttributes.addFlashAttribute("messageType", "success");
            redirectAttributes.addFlashAttribute("message", "Xóa danh mục thành công.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("messageType", "error");
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/admin/categories?page=" + currentPage;
    }
}
