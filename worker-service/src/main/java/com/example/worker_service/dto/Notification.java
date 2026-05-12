package com.example.worker_service.dto;

public record Notification(
        Long id,
        Long tripId,
        String recipientType,
        Long recipientId,
        String message,
        String status,
        Integer attempts,
        String createdAt
        //String processedAt
) {}