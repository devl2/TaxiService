package com.example.user_service.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "passengers")
public class Passenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phone;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Passenger(){}

    public Passenger(Long id, String name, String email, String phone, String password, LocalDateTime createdAt){
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.createdAt = createdAt;
    }

    public Long getId(){ return id; }

    public String getName(){ return name; }
    public void setName() { this.name = name; }

    public String getEmail(){ return email; }
    public void setEmail(){ this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhone(){ return phone; }
    public void setPhone(String phone) { this.phone = phone;}

    public LocalDateTime getCreatedAt() { return createdAt; }

    @PrePersist
    public void prePersist() { createdAt = LocalDateTime.now(); }
}
