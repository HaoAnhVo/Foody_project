package com.codegym.foody.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "cart_items")
@Data
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(optional = false)
    @JoinColumn(name = "food_id", nullable = false)
    private Food food;

    @Column(nullable = false)
    private Integer quantity;
}
