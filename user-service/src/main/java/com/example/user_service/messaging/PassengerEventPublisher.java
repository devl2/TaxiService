package com.example.user_service.messaging;

import com.example.user_service.dto.PassengerCreatedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class PassengerEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public PassengerEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishPassengerCreated(Long passengerId) {

        System.out.println("SENDING passenger.created: " + passengerId);

        PassengerCreatedEvent event = new PassengerCreatedEvent(passengerId);

        rabbitTemplate.convertAndSend(
                "user.exchange",
                "passenger.created",
                event
        );
    }
}