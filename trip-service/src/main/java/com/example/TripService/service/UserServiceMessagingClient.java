package com.example.TripService.service;

import com.example.TripService.dto.FindFreeDriverRequest;
import com.example.TripService.dto.FindFreeDriverResponse;
import com.example.TripService.dto.PassengerExistsRequest;
import com.example.TripService.dto.PassengerExistsResponse;
import com.example.TripService.dto.UpdateDriverStatusCommand;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

@Component
public class UserServiceMessagingClient {

    private final RabbitTemplate rabbitTemplate;

    public UserServiceMessagingClient(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public boolean passengerExists(Long passengerId) {
        PassengerExistsResponse reply = rabbitTemplate.convertSendAndReceiveAsType(
                "user.exchange",
                "user.passenger.exists",
                new PassengerExistsRequest(passengerId),
                new ParameterizedTypeReference<PassengerExistsResponse>() {}
        );
        return reply != null && reply.exists();
    }

    public Long findFreeDriver(Long tripId, Long passengerId) {
        FindFreeDriverResponse reply = rabbitTemplate.convertSendAndReceiveAsType(
                "user.exchange",
                "user.driver.find",
                new FindFreeDriverRequest(tripId, passengerId),
                new ParameterizedTypeReference<FindFreeDriverResponse>() {}
        );
        return reply == null ? null : reply.driverId();
    }

    public void updateDriverStatus(Long driverId, String status) {
        rabbitTemplate.convertAndSend(
                "user.exchange",
                "user.driver.status.update",
                new UpdateDriverStatusCommand(driverId, status)
        );
    }
}

