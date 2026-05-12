package com.example.user_service.listener;

import com.example.user_service.dto.FindFreeDriverRequest;
import com.example.user_service.dto.FindFreeDriverResponse;
import com.example.user_service.dto.PassengerExistsRequest;
import com.example.user_service.dto.PassengerExistsResponse;
import com.example.user_service.dto.UpdateDriverStatusCommand;
import com.example.user_service.entity.Driver;
import com.example.user_service.repository.PassengerRepository;
import com.example.user_service.service.DriverService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class UserRpcListener {

    private final PassengerRepository passengerRepository;
    private final DriverService driverService;

    public UserRpcListener(PassengerRepository passengerRepository, DriverService driverService) {
        this.passengerRepository = passengerRepository;
        this.driverService = driverService;
    }

    @RabbitListener(queues = "user.passenger.exists.queue")
    public PassengerExistsResponse handlePassengerExists(PassengerExistsRequest request) {
        boolean exists = request != null
                && request.passengerId() != null
                && passengerRepository.existsById(request.passengerId());
        return new PassengerExistsResponse(exists);
    }

    @RabbitListener(queues = "user.driver.find.queue")
    public FindFreeDriverResponse handleFindFreeDriver(FindFreeDriverRequest request) {
        Driver driver = driverService.assignAvailableDriver();
        return new FindFreeDriverResponse(driver == null ? null : driver.getId());
    }

    @RabbitListener(queues = "user.driver.status.update.queue")
    public void handleUpdateDriverStatus(UpdateDriverStatusCommand command) {
        if (command == null || command.driverId() == null || command.status() == null) {
            return;
        }
        driverService.updateStatus(command.driverId(), command.status());
    }
}

