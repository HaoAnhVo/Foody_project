package com.codegym.foody.service;

import com.codegym.foody.model.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IVoucherService extends IGeneralService<Voucher>{
    Page<Voucher> findWithPaginationAndKeyword(String keyword, Pageable pageable);
}
