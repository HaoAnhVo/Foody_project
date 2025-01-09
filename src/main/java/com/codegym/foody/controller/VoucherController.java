package com.codegym.foody.controller;

import com.codegym.foody.model.Voucher;
import com.codegym.foody.service.impl.VoucherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("/voucher")
public class VoucherController {
    private final VoucherService voucherService;
    public VoucherController(VoucherService voucherService){
        this.voucherService = voucherService;
    }

    @GetMapping("/list")
    public ModelAndView getVoucherList(){
        ModelAndView modelAndView = new ModelAndView("voucher/list");
        Iterable<Voucher> vouchers = voucherService.findAll();
        if (!vouchers.iterator().hasNext()) {
            modelAndView.addObject("errorMessage", "Không có mã giảm giá nào trong hệ thống!");
            modelAndView.addObject("vouchers", vouchers);

        }
        modelAndView.addObject("vouchers", vouchers);
        return modelAndView;
    }

    @GetMapping("/add")
    public ModelAndView showFormAdd() {
        ModelAndView modelAndView = new ModelAndView("voucher/add");
        modelAndView.addObject("voucher", new Voucher());
        return modelAndView;
    }

    @PostMapping("/add")
    public String addVoucher(@Validated @ModelAttribute Voucher voucher, BindingResult bindingResult, Model model) {
        if(bindingResult.hasFieldErrors()){
            return "voucher/add";
        }
        String code = voucher.getCode();
        if (voucherService.existVoucherCode(code)){
            model.addAttribute("errorMessage", "Mã giảm giá đã tồn tại!");
            return "voucher/add";
        }
        voucherService.save(voucher);
        return "redirect:/voucher/list";
    }

    @GetMapping("/edit")
    public ModelAndView showFormEdit(@RequestParam Long id){
        ModelAndView modelAndView = new ModelAndView("voucher/edit");
        Optional<Voucher> voucherOptional = voucherService.findById(id);
        if (!voucherOptional.isPresent()) {
            return new ModelAndView("voucher/list");
        }
        modelAndView.addObject("voucher",voucherOptional.get());
        return modelAndView;
    }

    @PostMapping("/edit")
    public String editVoucher(@RequestParam Long id, @Validated @ModelAttribute Voucher voucher, BindingResult bindingResult, Model model){
        if (bindingResult.hasFieldErrors()){
            return "voucher/edit";
        }
        Optional<Voucher> voucherOptional = voucherService.findById(id);
        Voucher existVoucher = voucherOptional.get();
        String code = voucher.getCode();
        if (!existVoucher.getCode().equals(code)){
            if (voucherService.existVoucherCode(code)){
                model.addAttribute( "errorMessage", "Mã giảm giá đã tồn tại! Mã giảm giá hiện tại " + existVoucher.getCode());
                //return "redirect:/voucher/edit?id=" + id;
                return "voucher/edit";
            }
        }
        voucherService.save(voucher);
        return "redirect:/voucher/list";
    }

    @GetMapping("/delete")
    public String deleteVoucher(@RequestParam Long id){
        voucherService.delete(id);
        return "redirect:/voucher/list";
    }

}
