package com.codegym.foody.repository;

import com.codegym.foody.model.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IVoucherRepository extends JpaRepository<Voucher, Long> {
    Voucher findByCode(String code);

    @Query("SELECT COUNT(b) FROM Voucher b where b.code = :code")
    Long existVoucherCode (@Param("code") String code);
}
