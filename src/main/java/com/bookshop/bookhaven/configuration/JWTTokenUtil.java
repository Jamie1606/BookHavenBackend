// Author		: Zay Yar Tun
// Admin No		: 2235035
// Class		: DIT/FT/2A/02
// Group		: 10
// Date			: 26.7.2023
// Description	: to generate and validate jwt token

package com.bookshop.bookhaven.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTTokenUtil {

    // Replace this secret key with your own secure key
    @Value("B00kh@veN2o@#zYttHs@#?2023c@2jwtt0kEn")
    private String SECRET_KEY;

    // Token expiration time (in milliseconds)
    @Value("3600000")
    private long EXPIRATION_TIME_MS;

    public String generateToken(String id, String email, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", id);
        claims.put("email", email);
        claims.put("role", role);
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                .signWith(getSigningKey())
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = parseToken(token);
            Date expirationDate = claims.getExpiration();
            return !expirationDate.before(new Date());
        } catch (Exception ex) {
            // Token parsing or validation failed
            return false;
        }
    }

    private Key getSigningKey() {
        // Convert the secret key to a cryptographic key using HMAC SHA-256 algorithm
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
}
