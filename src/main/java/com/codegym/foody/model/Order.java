package com.codegym.foody.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Vui lòng điền vào trường này")
    private LocalDateTime orderDate;

    @PrePersist
    public void prePersist() {
        if (orderDate == null) {
            this.orderDate = LocalDateTime.now();
        }
    }

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @NotNull(message = "Vui lòng điền vào trường này")
    @Min(value = 0, message = "Tổng giá tiền phải lớn hơn 0")
    private Double totalPrice;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;
}
