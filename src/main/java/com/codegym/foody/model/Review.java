package com.codegym.foody.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "reviews")
@Data
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Vui lòng điền vào trường này")
    @Min(value = 1, message = "Đánh giá phải trong khoảng 1 đến 5 sao")
    @Max(value = 5, message = "Đánh giá phải trong khoảng 1 đến 5 sao")
    private Integer rating;

    @NotBlank(message = "Vui lòng điền vào trường này")
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Cascade(CascadeType.ALL)
    private User user;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;
}
