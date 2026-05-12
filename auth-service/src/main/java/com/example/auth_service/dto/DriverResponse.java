package com.example.auth_service.dto;

public record DriverResponse(
        Long id,
        String email,
        String password,
        String status
) {}
