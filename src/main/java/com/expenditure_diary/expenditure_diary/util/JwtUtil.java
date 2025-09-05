package com.expenditure_diary.expenditure_diary.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final long jwtExpirationMs = 3600000;

    @Value("${jwt.secret_key}")
    private String secret;

    public String validateAndGetUsername(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secret) // must match the signing secret
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject(); // username we set during token generation
        } catch (JwtException e) {
            // Any parsing error means the token is invalid (expired, tampered, etc.)
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

}