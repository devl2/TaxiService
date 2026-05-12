package com.example.auth_service.dto;

public record PassengerResponse(
        Long id,
        String email,
        String password
) {}