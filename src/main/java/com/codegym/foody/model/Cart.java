package com.codegym.foody.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

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
    @Cascade(CascadeType.ALL)
    private User user;

    @OneToMany(mappedBy = "cart")
    @Cascade(CascadeType.ALL)
    private List<CartItem> cartItems;

}
