package com.example.expense_management.utils;

import com.example.expense_management.models.auth.UserEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

public class UserUtil {
    public static Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserEntity) {
            UserEntity user = (UserEntity) authentication.getPrincipal();
            Integer userId = user.getId();
            return userId;
        }
        return null; // hoặc return 0, return -1, vv.
    }
}
