package com.example.expense_management.controller;

import com.example.expense_management.dto.auth.AuthenticationRequest;
import com.example.expense_management.dto.auth.AuthenticationResponse;
import com.example.expense_management.dto.auth.RegisterRequest;
import com.example.expense_management.models.ResponseObject;
import com.example.expense_management.services.auth.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class UsersController {

    private final AuthenticationService authenticationService;

    @PostMapping("signin")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("signup")
    public ResponseEntity<ResponseObject> register(
         @Valid @RequestBody RegisterRequest request
    ) {
        return authenticationService.register(request);
    }

    @PostMapping("refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authenticationService.refreshToken(request, response);
    }

}
