package com.example.user_service.service;

import com.example.user_service.entity.Driver;
import com.example.user_service.entity.Passenger;
import com.example.user_service.messaging.PassengerEventPublisher;
import com.example.user_service.repository.PassengerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.PasswordAuthentication;
import java.util.List;

@Service
public class PassengerService {

    private final PassengerRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final PassengerEventPublisher eventPublisher;

    public PassengerService(PassengerRepository repository, PasswordEncoder passwordEncoder, PassengerEventPublisher eventPublisher) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
    }

    public Passenger createPassenger(Passenger passenger){
        if(passenger == null){
            throw new IllegalArgumentException("Запрос пуст");
        }

        passenger.setPassword(passwordEncoder.encode(passenger.getPassword()));
        Passenger saved = repository.save(passenger);

        eventPublisher.publishPassengerCreated(saved.getId());

        return saved;
    }

    public Passenger getPassenger(Long id){
        return repository.findById(id).orElse(null);
    }

    public List<Passenger> getAllPassengers(){
        return repository.findAll();
    }

    public Passenger save(Passenger passenger){
        return repository.save(passenger);
    }

    public Passenger getByEmail(String email) {
        return repository.findTopByEmailOrderByIdDesc(email)
                .orElseThrow(() -> new RuntimeException("Passenger not found"));
    }

}