package com.codegym.foody.repository;

import com.codegym.foody.model.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IVoucherRepository extends JpaRepository<Voucher, Long> {
    Page<Voucher> findByCodeContainingIgnoreCase(String keyword, Pageable pageable);
    @Query("SELECT COUNT(b) FROM Voucher b where b.code = :code")
    Long existVoucherCode (@Param("code") String code);
}
