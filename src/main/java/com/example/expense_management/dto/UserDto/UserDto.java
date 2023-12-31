package com.example.expense_management.dto.UserDto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class UserDto {

    private String username;
    private String email;
    private String address;
    private String phone;
    private String gender;
    private Date dateOfBirth;
    private String password;
}
