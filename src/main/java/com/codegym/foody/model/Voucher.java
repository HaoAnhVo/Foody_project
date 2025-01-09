package com.codegym.foody.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "voucher")
@Data
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Trường mã giảm giá không được để trống!")
    private String code;

    @NotNull(message = "Trường giá trị giảm giá không được để trống!")
    @Column(name = "discount_value", nullable = false)
    private Double discountValue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiscountType discountType;

    @Column(name = "start_date")
    @NotNull(message = "Trường ngày bắt đầu không được để trống!")
    private LocalDate startDate;

    @Column(name = "end_date")
    @NotNull(message = "Trường ngày kết thúc không được để trống!")
    private LocalDate endDate;

    @Column(nullable = true)
    private Double minOrderValue;

    @Column(nullable = true)
    private Integer usageLimit;

    @Column(nullable = false)
    private Boolean isActive = true;

}
