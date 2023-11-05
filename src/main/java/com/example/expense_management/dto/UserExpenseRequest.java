package com.example.expense_management.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class UserExpenseRequest {
    @NotEmpty(message = "Bạn cần đăng nhập để thực hiện chức năng này")
    private Integer userId;
    private Integer categoryId;
    private Integer amount;

    @Length(max = 255, message = "Tên không được quá 255 ký tự")
    private String name;

    @Length(max = 255, message = "Ghi chú không được quá 255 ký tự")
    private String note;
}
