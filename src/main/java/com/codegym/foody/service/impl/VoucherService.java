package com.codegym.foody.service.impl;

import com.codegym.foody.model.Restaurant;
import com.codegym.foody.model.Voucher;
import com.codegym.foody.repository.IVoucherRepository;
import com.codegym.foody.service.IVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoucherService implements IVoucherService {
    @Autowired
    private IVoucherRepository voucherRepository;

    @Override
    public Page<Voucher> findWithPaginationAndKeyword(String keyword, Pageable pageable) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            return voucherRepository.findByCodeContainingIgnoreCase(keyword,pageable);
        }
        return voucherRepository.findAll(pageable);
    }

    @Override
    public void save(Voucher voucher) {
        voucherRepository.save(voucher);
    }

    @Override
    public void update(Voucher voucher) {
        if (voucherRepository.existsById(voucher.getId())) {
            voucherRepository.save(voucher);
        } else {
            throw new RuntimeException("Không tìm thấy voucher");
        }
    }

    @Override
    public void delete(Long id) {
        voucherRepository.deleteById(id);
    }

    @Override
    public long getTotal() {
        return voucherRepository.count();
    }

    @Override
    public Voucher getById(Long id) {
        return voucherRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy voucher"));
    }

    @Override
    public int getPage(Long id) {
        int pageSize = 10;
        List<Voucher> sortedVouchers = voucherRepository.findAll();
        int index = sortedVouchers.indexOf(voucherRepository.findById(id).orElse(null));
        return index / pageSize;
    }

    public boolean existVoucherCode(String code){
        Long result = voucherRepository.existVoucherCode(code);
        return result != null && result > 0;
    }
}
