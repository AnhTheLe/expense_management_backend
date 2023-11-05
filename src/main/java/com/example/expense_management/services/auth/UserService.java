package com.example.expense_management.services.auth;


import com.example.expense_management.dto.UserDto.UserDto;
import com.example.expense_management.models.ResponseObject;
import com.example.expense_management.models.auth.Role;
import com.example.expense_management.models.auth.UserEntity;
import org.springframework.http.ResponseEntity;

public interface UserService {
    UserEntity saveUser(UserEntity user);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);
    public ResponseEntity<ResponseObject> updateUser(Integer id, UserDto userRequest);
}
