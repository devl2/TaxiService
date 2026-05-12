package com.example.worker_service.listener;

import com.example.worker_service.service.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TripStatusChangedListener {

    private final NotificationService notificationService;

    public TripStatusChangedListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public record TripStatusChangedEvent(Long tripId, String status, Long passengerId, Long driverId) {}

    @RabbitListener(queues = "worker.trip.status.queue.v2")
    public void handleTripStatusChanged(TripStatusChangedEvent event) {
        if (event == null || event.tripId() == null || event.status() == null) {
            return;
        }

        System.out.println("RECEIVED trip.status.changed: tripId=" + event.tripId() + " status=" + event.status());

        if (event.passengerId() != null) {
            notificationService.addNotification(
                    event.tripId(),
                    "PASSENGER",
                    event.passengerId(),
                    "Trip " + event.tripId() + " status changed to " + event.status()
            );
        }
    }
}

