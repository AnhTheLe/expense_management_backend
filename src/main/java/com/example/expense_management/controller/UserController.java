package com.example.expense_management.controller;

import com.example.expense_management.dto.UserDto.CurrentUserDto;
import com.example.expense_management.dto.UserDto.UserDto;
import com.example.expense_management.dto.auth.AuthenticationRequest;
import com.example.expense_management.dto.auth.AuthenticationResponse;
import com.example.expense_management.dto.auth.RegisterRequest;
import com.example.expense_management.models.ResponseObject;
import com.example.expense_management.models.auth.UserEntity;
import com.example.expense_management.repositories.UserRepository;
import com.example.expense_management.services.auth.AuthenticationService;
import com.example.expense_management.services.auth.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import static com.example.expense_management.utils.UserUtil.getCurrentUserId;

@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final UserRepository userRepository;

    @PutMapping("/update-user/{id}")
    public ResponseEntity<ResponseObject> updateUser(@PathVariable Integer id, @RequestBody UserDto userRequest){
        return userService.updateUser(id, userRequest);
    }

    @GetMapping("/current-user")
    public ResponseEntity<ResponseObject> getCurrentUser(@RequestParam(name = "username") String username) {
        Integer currUserId = getCurrentUserId();
        log.info("Current user id: " + currUserId);
        if (currUserId == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("error", "Bạn chưa đăng nhập", ""));
        }
        Optional<UserEntity> currUser = null;

        currUser = userRepository.findByUsername(username);
        if(currUser.isEmpty() || !Objects.equals(currUser.get().getId(), currUserId)) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("error", "Không tìm thấy người dùng", ""));
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseObject("success", "", currUser));
    }

}
