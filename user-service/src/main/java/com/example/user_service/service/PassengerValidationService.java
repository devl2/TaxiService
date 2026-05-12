package com.example.user_service.service;

import com.example.user_service.repository.PassengerCacheRepository;
import org.springframework.stereotype.Service;

@Service
public class PassengerValidationService {

    private final PassengerCacheRepository repository;

    public PassengerValidationService(PassengerCacheRepository repository) {
        this.repository = repository;
    }

    public void validate(Long passengerId) {

        if (!repository.existsById(passengerId)) {
            throw new IllegalArgumentException("Passenger does not exist");
        }
    }
}
