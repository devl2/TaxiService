package com.example.auth_service.service;

import com.example.auth_service.dto.*;
import com.example.auth_service.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpStatus;

@Service
public class AuthService {

    private final JwtService jwtService;
    private final RestTemplate restTemplate;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            JwtService jwtService,
            RestTemplate restTemplate, PasswordEncoder passwordEncoder
    ) {
        this.jwtService = jwtService;
        this.restTemplate = restTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse loginPassenger(LoginRequest request) {
        try {
            PassengerResponse user = restTemplate.getForObject(
                    "http://user-service:8081/passengers/email?email=" + request.email(),
                    PassengerResponse.class
            );

            if (user == null || !passwordEncoder.matches(request.password(), user.password())) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
            }

            String token = jwtService.generateToken(user.id(), "PASSENGER");
            return new LoginResponse(token);
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
    }

    public LoginResponse loginDriver(LoginRequest request) {
        try {
            DriverResponse user = restTemplate.getForObject(
                    "http://user-service:8081/drivers/email?email=" + request.email(),
                    DriverResponse.class
            );

            if (user == null || !passwordEncoder.matches(request.password(), user.password())) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
            }

            String token = jwtService.generateToken(user.id(), "DRIVER");
            return new LoginResponse(token);
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
    }
}