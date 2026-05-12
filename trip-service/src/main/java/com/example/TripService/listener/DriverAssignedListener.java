package com.example.TripService.listener;

import com.example.TripService.dto.DriverAssignedEvent;
import com.example.TripService.entity.Trip;
import com.example.TripService.enums.TripStatus;
import com.example.TripService.repository.TripRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class DriverAssignedListener {

    private final TripRepository repository;

    public DriverAssignedListener(TripRepository repository) {
        this.repository = repository;
    }

    @RabbitListener(queues = "trip.queue")
    public void handleDriverAssigned(DriverAssignedEvent event) {
        if (event == null || event.tripId() == null || event.driverId() == null) {
            return;
        }

        Trip trip = repository.findById(event.tripId())
                .orElseThrow();

        trip.setDriverId(event.driverId());
        trip.setStatus(TripStatus.ACCEPTED);

        repository.save(trip);

        System.out.println("DRIVER ASSIGNED");
    }

}