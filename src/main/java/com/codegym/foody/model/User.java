package com.codegym.foody.model;

import com.codegym.foody.model.enumable.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên đăng nhập là bắt buộc")
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @NotBlank(message = "Mật khẩu là bắt buộc")
    @Size(min = 6, message = "Mật khẩu tối thiểu phải có 6 ký tự")
    @Column(name = "password", nullable = false)
    private String password;

    @NotBlank(message = "Họ tên là bắt buộc")
    @Column(name = "full_name", nullable = false)
    private String fullname;

    @NotBlank(message = "Email là bắt buộc")
    @Column(nullable = false, unique = true)
    @Email(message = "Sai định dạng email")
    private String email;

    @NotBlank(message = "Số điện thoại là bắt buộc")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Sai định dạng số điện thoại")
    @Column(name = "phone", unique = true, nullable = false)
    private String phone;

    @NotBlank(message = "Địa chỉ là bắt buộc")
    @Column(name = "address", nullable = false)
    private String address;

    @NotNull(message = "Vui lòng chọn vai trò")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.CUSTOMER;

    @Column(nullable = false)
    private Boolean status = true;
}
