package com.example.mvc.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtTokenProvider {

    private final SecretKey jwtSecret;
    private final long jwtExpirationMs;

    /**
     * Constructor to initialize secret key and expiration time from properties.
     *
     * @param secret          the secret key from application.properties
     * @param jwtExpirationMs the expiration time in milliseconds from application.properties
     */
    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long jwtExpirationMs) {
        if (secret == null || secret.isBlank() || secret.length() < 32) {
            throw new IllegalArgumentException("JWT secret must be at least 32 characters long");
        }
        this.jwtSecret = Keys.hmacShaKeyFor(secret.getBytes());
        this.jwtExpirationMs = jwtExpirationMs;
    }

    /**
     * Generate a JWT token for a specific username.
     *
     * @param username the username to include in the token
     * @return the generated JWT token
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date()) // Issue time
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)) // Expiry time
                .signWith(jwtSecret, SignatureAlgorithm.HS256) // Sign with secret key
                .compact();
    }

    /**
     * Validate a JWT token.
     *
     * @param token the JWT token to validate
     * @return true if the token is valid, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtSecret) // Use the shared secret key
                    .build()
                    .parseClaimsJws(token); // Validate the token
            return true; // Token is valid
        } catch (ExpiredJwtException e) {
            System.err.println("Expired JWT token: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.err.println("Unsupported JWT token: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.err.println("Malformed JWT token: " + e.getMessage());
        } catch (SignatureException e) {
            System.err.println("Invalid JWT signature: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Empty or null JWT token: " + e.getMessage());
        }
        return false; // Token is invalid
    }

    /**
     * Extract the username from a JWT token.
     *
     * @param token the JWT token
     * @return the username extracted from the token
     */
    public String getUsernameFromJwt(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtSecret) // Use the shared secret key
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject(); // Return the username (subject)
    }
}
