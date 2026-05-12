package com.example.user_service.record;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record User(String username, String email, String phone, String createdAt, String role) {
    public static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public User {
        if(username == null || username.isBlank()){
            throw new IllegalArgumentException("Username не может быть пустым или null");
        }
        if(email == null || email.isBlank()){
            throw new IllegalArgumentException("Email не может быть пустым или null");
        }
        if(phone == null || phone.isBlank()){
            throw new IllegalArgumentException("Phone не может быть пустым или null");
        }
        if(!username.matches("^[a-zA-Z0-9_]+$")){
            throw new IllegalArgumentException("Поле username должно содержать только латинские буквы, цифры и подчёркивание");
        }
        if(username.length() < 3 || username.length() > 20){
            throw new IllegalArgumentException("Поле username должно быть от 3 до 20 символов");
        }
        if (!email.matches("^[a-zA-Z0-9_]+@?[a-zA-Z]+\\.[a-zA-Z]+$")) {
            throw new IllegalArgumentException("Введен неверный email");
        }
        if(phone.length() < 1 || phone.length() > 12){
            throw new IllegalArgumentException("Поле phone должно быть от 1 до 11 символов");
        }
        if (!phone.matches("^[0-9]+$")){
            throw new IllegalArgumentException("Введен неверный номер телефона");
        }
    }

    public static User create(String username, String email, String phone, String role) {
        String createdAt = LocalDateTime.now().format(ISO_FORMATTER);
        return new User(username, email, phone, createdAt, role);
    }

    public String format() {
        return String.format("%s <%s> (%s) %s", username, email, phone, createdAt);
    }
}
