package com.codegym.foody.model;

import com.codegym.foody.model.enumable.ApprovalStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalTime;

@Entity
@Table(name = "restaurants")
@Data
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên nhà hàng là bắt buộc")
    private String name;

    @NotBlank(message = "Số điện thoại nhà hàng là bắt buộc")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Sai định dạng số điện thoại")
    @Column(unique = true, nullable = false)
    private String phone;

    @NotBlank(message = "Địa chỉ nhà hàng là bắt buộc")
    private String address;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull(message = "Vui lòng nhập giờ mở cửa")
    private LocalTime openingTime;

    @NotNull(message = "Vui lòng nhập giờ đóng cửa")
    private LocalTime closingTime;

    @Column(nullable = false)
    private Boolean status = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING;
}
