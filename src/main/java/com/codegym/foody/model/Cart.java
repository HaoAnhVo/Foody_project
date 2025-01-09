package com.codegym.foody.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "carts")
@Data
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItem> cartItems;

    // Method to calculate total price (optional)
    public Double calculateTotalPrice() {
        return cartItems.stream()
                .mapToDouble(item -> item.getMenu().getPrice() * item.getQuantity())
                .sum();
    }
}
