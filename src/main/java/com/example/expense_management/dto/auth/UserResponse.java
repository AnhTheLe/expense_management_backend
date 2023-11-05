package com.example.expense_management.dto.auth;

import com.example.expense_management.models.enums.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private Long id;
    private String username;
    private String avatar;
    private String fullName;

    @Enumerated(EnumType.STRING)
    private Gender gender;
}
