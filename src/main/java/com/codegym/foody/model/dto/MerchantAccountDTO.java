package com.codegym.foody.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MerchantAccountDTO {
    @NotBlank(message = "Tên đăng nhập là bắt buộc")
    private String username;

    @NotBlank(message = "Mật khẩu là bắt buộc")
    @Size(min = 6, message = "Mật khẩu tối thiểu phải có 6 ký tự")
    private String password;

    @NotBlank(message = "Họ tên là bắt buộc")
    private String fullname;

    @NotBlank(message = "Email là bắt buộc")
    @Email(message = "Sai định dạng email")
    private String email;

    @NotBlank(message = "Số điện thoại là bắt buộc")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Sai định dạng số điện thoại")
    private String phone;

    @NotBlank(message = "Địa chỉ là bắt buộc")
    private String address;

    private Boolean status = false;
}
