package com.example.TripService.service;

import com.example.TripService.repository.PassengerCacheRepository;
import org.springframework.stereotype.Service;

@Service
public class PassengerValidationService {

    private final PassengerCacheRepository repository;
    private final UserServiceMessagingClient userServiceMessagingClient;

    public PassengerValidationService(PassengerCacheRepository repository,
                                      UserServiceMessagingClient userServiceMessagingClient) {
        this.repository = repository;
        this.userServiceMessagingClient = userServiceMessagingClient;
    }

    public void validate(Long passengerId) {
        boolean exists = userServiceMessagingClient.passengerExists(passengerId);

        if (exists && !repository.existsById(passengerId)) {
            repository.save(new com.example.TripService.entity.PassengerCache(passengerId));
        }

        if (!exists) {
            throw new IllegalArgumentException("Passenger does not exist");
        }
    }
}
