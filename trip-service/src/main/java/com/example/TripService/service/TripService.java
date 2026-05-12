package com.example.TripService.service;

import com.example.TripService.dto.DriverAssignedEvent;
import com.example.TripService.dto.TripCreatedEvent;
import com.example.TripService.dto.RateTripRequest;
import com.example.TripService.dto.TripStatusChangedEvent;
import com.example.TripService.entity.Trip;
import com.example.TripService.enums.TripStatus;
import com.example.TripService.repository.TripRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TripService {

    private static final Double TARIF = 2.5;

    private final TripRepository repository;
    private final RabbitTemplate rabbitTemplate;
    private final PassengerValidationService passengerValidationService;
    private final UserServiceMessagingClient userServiceMessagingClient;

    public TripService(TripRepository repository,
                       RabbitTemplate rabbitTemplate,
                       PassengerValidationService passengerValidationService,
                       UserServiceMessagingClient userServiceMessagingClient) {
        this.repository = repository;
        this.rabbitTemplate = rabbitTemplate;
        this.passengerValidationService = passengerValidationService;
        this.userServiceMessagingClient = userServiceMessagingClient;
    }

    public Trip createTrip(Long passengerId, String origin, String destination) {

        passengerValidationService.validate(passengerId);

        Double distance = Math.random() * 15 + 1;
        Double price = distance * TARIF;

        Trip trip = new Trip(
                null,
                passengerId,
                null,
                TripStatus.CREATED,
                origin,
                destination,
                price,
                distance,
                null,
                null
        );

        Trip saved = repository.save(trip);

        Long driverId = userServiceMessagingClient.findFreeDriver(saved.getId(), passengerId);

        if (driverId != null) {
            saved.setDriverId(driverId);
            saved.setStatus(TripStatus.DRIVER_ASSIGNED);
            saved = repository.save(saved);

            userServiceMessagingClient.updateDriverStatus(driverId, "BUSY");

            rabbitTemplate.convertAndSend(
                    "trip.exchange",
                    "trip.status.changed",
                    new TripStatusChangedEvent(saved.getId(), TripStatus.DRIVER_ASSIGNED, saved.getPassengerId(), saved.getDriverId())
            );
        }

        return saved;
    }

    public Trip updateStatus(Long id, TripStatus newStatus) {

        Trip trip = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Поездка не найдена"));

        trip.setStatus(newStatus);

        if (TripStatus.ENDED.equals(newStatus)) {
            trip.setUpdatedAt(LocalDateTime.now());
            if (trip.getDriverId() != null) {
                userServiceMessagingClient.updateDriverStatus(trip.getDriverId(), "AVAILABLE");
            }
        }

        if (TripStatus.CANCELLED.equals(newStatus) && trip.getDriverId() != null) {
            userServiceMessagingClient.updateDriverStatus(trip.getDriverId(), "AVAILABLE");
        }

        Trip saved = repository.save(trip);

        rabbitTemplate.convertAndSend(
                "trip.exchange",
                "trip.status.changed",
                new TripStatusChangedEvent(saved.getId(), newStatus, saved.getPassengerId(), saved.getDriverId())
        );

        return saved;
    }

    public Trip getTrip(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<Trip> getTripsByPassenger(Long passengerId) {
        return repository.findByPassengerId(passengerId);
    }

    public Trip rateTrip(Long id, Integer rating) {
        if (rating == null || rating < 1 || rating > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rating must be between 1 and 5");
        }

        Trip trip = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Поездка не найдена"));

        if (!TripStatus.ENDED.equals(trip.getStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Оценить можно только завершенную поездку");
        }

        trip.setRating(rating);
        return repository.save(trip);
    }
}