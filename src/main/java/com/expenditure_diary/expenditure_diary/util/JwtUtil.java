package com.expenditure_diary.expenditure_diary.util;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private final long jwtExpirationMs = 3600000;

    private final SecretKey key = Keys.hmacShaKeyFor(
        Base64.getDecoder().decode("yI5uXwrEt9pR7hcgV8nE0Wp3K1e8VsE6n9t7WRrN6uGtdjGQYq+6sZGc1K3qC3phFVzxEWExyqAHaVeryLongSecretKeyThatIsAtLeastSixtyFourCharactersLongForHS512Encryption")
    );

    public String generateToken(String username, long userId) {
        Map<String,Object> claim = new HashMap<>();
        claim.put("user_id", userId);

        return Jwts.builder()
                .setSubject(username)
                .addClaims(claim)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUsernameFromJwt(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateJwt(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}