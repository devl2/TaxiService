package com.example.TripService.service;

import com.example.TripService.dto.TripCreatedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class TripPublisher {

    private final RabbitTemplate rabbitTemplate;

    public TripPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(String routingKey, Object event) {
        rabbitTemplate.convertAndSend(
                "trip.exchange",
                routingKey,
                event
        );
    }
}
