package com.codegym.foody.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "menus")
@Data
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên món ăn là bắt buộc")
    private String name;

    @NotBlank(message = "Mô tả món ăn là bắt buộc")
    private String description;

    @NotNull(message = "Giá sản phẩm là bắt buộc")
    @Min(value = 0, message = "Giá sản phẩm phải lớn hơn 0")
    private BigDecimal price;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @OneToMany(mappedBy = "menu")
    @Cascade(CascadeType.ALL)
    private List<Review> reviews;

    @NotNull(message = "Trạng thái không được để trống")
    @Column(name = "status", nullable = false)
    private boolean status;
}
