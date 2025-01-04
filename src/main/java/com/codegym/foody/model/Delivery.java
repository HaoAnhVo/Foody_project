package com.codegym.foody.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "deliveries")
@Data
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(optional = false)
    @JoinColumn(name = "delivery_person_id", nullable = false)
    private User deliveryPerson;

    @Column(nullable = false, length = 255)
    private String deliveryAddress;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    private LocalDateTime estimatedTime;

    public enum Status {
        PENDING, IN_PROGRESS, DELIVERED
    }
}
