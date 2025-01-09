package com.codegym.foody.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "restaurants")
@Data
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Vui lòng điền vào trường này")
    private String name;

    @NotBlank(message = "Vui lòng điền vào trường này")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Sai định dạng số điện thoại")
    @Column(unique = true, nullable = false)
    private String phone;

    @NotBlank(message = "Vui lòng điền vào trường này")
    private String address;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<Menu> menus;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<Order> orders;

    @NotNull(message = "Vui lòng nhập giờ mở cửa")
    private LocalTime openingTime;

    @NotNull(message = "Vui lòng nhập giờ đóng cửa")
    private LocalTime closingTime;
}
