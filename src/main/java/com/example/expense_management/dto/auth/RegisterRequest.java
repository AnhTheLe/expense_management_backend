package com.example.expense_management.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotEmpty(message = "Tên đăng nhập không được để trống")
    @Length(min = 6, max = 20, message = "Tên đăng nhập từ 6 đến 20 ký tự")
    private String username;

    @NotEmpty(message = "Email không được để trống")
    @Email(message = "Địa chỉ email không hợp lệ")
    private String email;

    @NotEmpty(message = "Mật khẩu không được để trống")
    @Length(min = 6, message = "Mật khẩu tối thiểu 6 ký tự")
    private String password;

    @NotEmpty(message = "Số điện thoại không được để trống")
    @Length(min = 10, message = "Số điện thoại không đúng định dạng")
    private String phone;
}
