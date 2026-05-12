package com.example.TripService.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class TripEventProducer {

    private final RabbitTemplate rabbitTemplate;

    public TripEventProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(String routingKey, Object event) {
        rabbitTemplate.convertAndSend(
                "trip.exchange",
                routingKey,
                event
        );
    }
}
