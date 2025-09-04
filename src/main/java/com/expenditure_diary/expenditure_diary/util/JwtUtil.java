package com.expenditure_diary.expenditure_diary.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;

@Component
public class JwtUtil {

    private final long jwtExpirationMs = 3600000;

    private final SecretKey key = Keys.hmacShaKeyFor(
        Base64.getDecoder().decode("yI5uXwrEt9pR7hcgV8nE0Wp3K1e8VsE6n9t7WRrN6uGtdjGQYq+6sZGc1K3qC3phFVzxEWExyqAHaVeryLongSecretKeyThatIsAtLeastSixtyFourCharactersLongForHS512Encryption")
    );

    public String validateAndGetUsername(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(key) // must match the signing secret
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject(); // username we set during token generation
        } catch (JwtException e) {
            // Any parsing error means the token is invalid (expired, tampered, etc.)
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

}