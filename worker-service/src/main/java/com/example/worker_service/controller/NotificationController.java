package com.example.worker_service.controller;

import com.example.worker_service.dto.NotificationRequest;
import com.example.worker_service.entity.Notification;
import com.example.worker_service.service.NotificationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public Long addNotification(@RequestBody NotificationRequest request) {

        return notificationService.addNotification(
                request.tripId(),
                request.recipientType(),
                request.recipientId(),
                request.message()
        );
    }

    @GetMapping
    public List<Notification> getNotificationsByTrip(@RequestParam Long tripId) {

        return notificationService.getNotificationsByTrip(tripId);
    }
}