package com.codegym.foody.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Vui lòng điền vào trường này")
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @NotBlank(message = "Vui lòng điền vào trường này")
    @Size(min = 6, message = "Mật khẩu tối thiểu phải có 6 ký tự")
    @Column(name = "password", nullable = false)
    private String password;

    @NotBlank(message = "Vui lòng điền vào trường này")
    @Column(name = "full_name", nullable = false)
    private String fullname;

    @Column(nullable = false, unique = true)
    @Email(message = "Sai định dạng email")
    private String email;

    @NotBlank(message = "Vui lòng điền vào trường này")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Sai định dạng số điện thoại")
    @Column(name = "phone", unique = true, nullable = false)
    private String phone;

    @NotBlank(message = "Vui lòng điền vào trường này")
    @Column(name = "address", nullable = false)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private Boolean status = true;

    public enum Role {
        CUSTOMER, EMPLOYEE, ADMIN, DELIVERY_PERSON
    }
}