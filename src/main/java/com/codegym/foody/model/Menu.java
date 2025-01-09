package com.codegym.foody.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "menus")
@Data
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Vui lòng điền vào trường này")
    private String name;

    @NotBlank(message = "Vui lòng điền vào trường này")
    private String description;

    @NotNull(message = "Vui lòng điền vào trường này")
    @Min(value = 0, message = "Giá sản phẩm phải lớn hơn 0")
    private Double price;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
    private List<Review> reviews;

    @Column(name = "status", nullable = false)
    private boolean status;
}
