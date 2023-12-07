package com.example.expense_management.services.auth;

import com.example.expense_management.dto.UserDto.UserDto;
import com.example.expense_management.models.ResponseObject;
import com.example.expense_management.models.auth.Role;
import com.example.expense_management.models.auth.UserEntity;
import com.example.expense_management.models.enums.Gender;
import com.example.expense_management.models.enums.RoleType;
import com.example.expense_management.repositories.RoleRepository;
import com.example.expense_management.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static com.example.expense_management.utils.UserUtil.getCurrentUserId;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private Integer currentUserId = getCurrentUserId();

    List<UserEntity> findAllUser() {
        return userRepository.findAll();
    }

    @Override
    public UserEntity saveUser(UserEntity user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public ResponseEntity<ResponseObject> updateUser(Integer id, UserDto userRequest) {

        // Thực hiện công việc nếu ID giống hoặc người dùng có vai trò ADMIN
        Optional<UserEntity> user = userRepository.findById(id);
        if (userRequest.getPhone() != null) {
            user.get().setPhone(userRequest.getPhone());
        }
        if (userRequest.getGender() != null) {
            user.get().setGender(Gender.valueOf(userRequest.getGender()));
        }
        if (userRequest.getEmail() != null) {
            user.get().setEmail(userRequest.getEmail());
        }
        if (userRequest.getAddress() != null) {
            user.get().setAddress(userRequest.getAddress());
        }
        if(userRequest.getDateOfBirth() != null) {
            user.get().setDateOfBirth(userRequest.getDateOfBirth());
        }

        userRepository.save(user.get());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseObject("success", "", user.get()));

    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        UserEntity user = userRepository.findByUsername(username).orElse(null);
        Optional<Role> role = roleRepository.findByName(roleName);
        if (user != null && role.isPresent()) {
            if (user.getRoles() == null) {
                user.setRoles(new HashSet<>()); // Khởi tạo roles là một HashSet mới nếu roles là null
            }
            user.getRoles().add(role.get());
        }
    }

}