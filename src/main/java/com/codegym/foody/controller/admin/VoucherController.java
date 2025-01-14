package com.codegym.foody.controller.admin;

import com.codegym.foody.model.Voucher;
import com.codegym.foody.model.dto.PaginationResult;
import com.codegym.foody.service.impl.PaginationService;
import com.codegym.foody.service.impl.VoucherService;
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
@RequestMapping("/admin/vouchers")
public class VoucherController {
    @Autowired
    private VoucherService voucherService;

    @Autowired
    private PaginationService paginationService;

    @GetMapping
    public String getListVoucher(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 @RequestParam(required = false) String keyword,
                                 Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Voucher> voucherPage = voucherService.findWithPaginationAndKeyword(keyword, pageable);
        PaginationResult paginationResult = paginationService.calculatePagination(voucherPage);

        model.addAttribute("voucherPage", voucherPage);
        model.addAttribute("vouchers", voucherPage.getContent());
        model.addAttribute("currentPage", paginationResult.getCurrentPage());
        model.addAttribute("totalPages", paginationResult.getTotalPages());
        model.addAttribute("pageNumbers", paginationResult.getPageNumbers());
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", "vouchers");
        return "admin/vouchers/list";

    }

    @GetMapping("/create")
    public String createVoucherForm (Model model) {
        model.addAttribute("voucher", new Voucher());
        return "admin/vouchers/form";
    }

    @PostMapping("/create")
    public String createVoucher (@Valid @ModelAttribute Voucher voucher,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes){
        if (voucherService.existVoucherCode(voucher.getCode())){
            redirectAttributes.addFlashAttribute("messageType", "error");
            redirectAttributes.addAttribute("message", "Mã giảm giá đã tồn tại.");
            return "redirect:/admin/vouchers/create";
        }
        if (bindingResult.hasErrors()) {
            return "admin/vouchers/form";
        }
        voucherService.save(voucher);
        long totalVouchers = voucherService.getTotal();
        int pageSize = 10;
        int lastPage = (int) Math.ceil((double) totalVouchers / pageSize) - 1;
        redirectAttributes.addFlashAttribute("messageType", "success");
        redirectAttributes.addFlashAttribute("message", "Thêm mã giảm giá mới thành công.");
        return "redirect:/admin/vouchers?page=" + lastPage;
    }

    @GetMapping("/edit/{id}")
    public String editVoucherForm(@PathVariable Long id, Model model) {
        Voucher voucher = voucherService.getById(id);
        model.addAttribute("voucher", voucher);
        return "admin/vouchers/form";
    }

    @PostMapping("/edit")
    public String editVoucher(@Valid @ModelAttribute Voucher voucher, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "admin/vouchers/form";
        }
        String code = voucher.getCode();
        Long id = voucher.getId();
        Voucher existVoucher = voucherService.getById(id);
        if (!existVoucher.getCode().equals(code)){
            if (voucherService.existVoucherCode(code)){
                redirectAttributes.addFlashAttribute("messageType", "error");
                redirectAttributes.addAttribute("message", "Mã giảm giá đã tồn tại.");
                return "redirect:/admin/vouchers/edit/" + id;
            }
        }
        voucherService.update(voucher);
        redirectAttributes.addFlashAttribute("messageType", "success");
        redirectAttributes.addFlashAttribute("message", "Cập nhật thông tin mã giảm giá thành công.");
        return "redirect:/admin/vouchers";
    }

    @GetMapping("delete/{id}")
    public String deleteVoucher (@PathVariable("id") Long id, RedirectAttributes redirectAttributes,
                                 @RequestParam(value = "currentPage", defaultValue = "0") int currentPage) {
        voucherService.delete(id);

        long totalCategories = voucherService.getTotal();
        int pageSize = 10;
        int lastPage = (int) Math.ceil((double) totalCategories / pageSize) - 1;

        if (currentPage > lastPage) {
            currentPage = Math.max(0, lastPage);
        }

        redirectAttributes.addFlashAttribute("messageType", "success");
        redirectAttributes.addFlashAttribute("message", "Xóa mã giảm giá thành công.");
        return "redirect:/admin/vouchers?page=" + currentPage;
    }

}
