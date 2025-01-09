package com.codegym.foody.service.impl;

import com.codegym.foody.model.Voucher;
import com.codegym.foody.repository.IVoucherRepository;
import com.codegym.foody.service.IVoucherService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class VoucherService implements IVoucherService {
    private final IVoucherRepository iVoucherRepository;
    public VoucherService(IVoucherRepository iVoucherRepository) {
        this.iVoucherRepository = iVoucherRepository;
    }
    @Override
    public Iterable<Voucher> findAll() {
        return iVoucherRepository.findAll();
    }

    @Override
    public Optional<Voucher> findById(Long id) {
        return iVoucherRepository.findById(id);
    }

    @Override
    public void save(Voucher voucher) {
        iVoucherRepository.save(voucher);
    }

    @Override
    public void delete(Long id) {
        iVoucherRepository.deleteById(id);
    }

    public boolean isVoucherValid(Voucher voucher) {
        LocalDate today = LocalDate.now();
        return voucher.getIsActive() &&
                today.isAfter(voucher.getStartDate()) &&
                today.isBefore(voucher.getEndDate());
    }

    public Voucher getVoucherByCode(String code) {
        return iVoucherRepository.findByCode(code);
    }

    public boolean existVoucherCode(String code){
        Long result = iVoucherRepository.existVoucherCode(code);
        return result != null && result > 0;
    }
}
