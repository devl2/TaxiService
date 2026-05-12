package com.example.TripService.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String SECRET;

    private Key key() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public Claims extract(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validate(String token) {
        try {
            extract(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
