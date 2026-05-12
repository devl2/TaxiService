package com.example.auth_service.controller;

import com.example.auth_service.dto.LoginRequest;
import com.example.auth_service.dto.LoginResponse;
import com.example.auth_service.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/passenger/login")
    public LoginResponse loginPassenger(@RequestBody LoginRequest request) {
        return authService.loginPassenger(request);
    }

    @PostMapping("/driver/login")
    public LoginResponse loginDriver(@RequestBody LoginRequest request) {
        return authService.loginDriver(request);
    }
}