package com.example.TripService.listener;

import com.example.TripService.dto.PassengerCreatedEvent;
import com.example.TripService.entity.PassengerCache;
import com.example.TripService.repository.PassengerCacheRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PassengerEventListener {

    private final PassengerCacheRepository repository;

    public PassengerEventListener(PassengerCacheRepository repository) {
        this.repository = repository;
    }

    @RabbitListener(queues = "trip.passenger.events.queue")
    public void handlePassengerCreated(PassengerCreatedEvent event) {
        repository.save(new PassengerCache(event.passengerId()));
    }
}
