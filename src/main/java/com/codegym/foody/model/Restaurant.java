package com.codegym.foody.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "restaurant")
@Data
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(nullable = false, length = 15)
    private String phoneNumber;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private Double rating = 0.0;

    @Column(nullable = false)
    private Boolean status = true;
}
