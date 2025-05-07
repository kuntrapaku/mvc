package com.example.mvc.service;

import com.example.mvc.model.User;
import com.example.mvc.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthService {

    // Use a shared secret key from configuration to ensure consistency
    private final SecretKey SECRET_KEY;
    private final long EXPIRATION_TIME;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Constructor to inject secret key and expiration time
    public AuthService(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.expiration}") long expirationTime) {
        this.SECRET_KEY = Keys.hmacShaKeyFor(secretKey.getBytes()); // Generate a secure key from the shared secret
        this.EXPIRATION_TIME = expirationTime; // Set expiration time from properties
    }

    /**
     * Register a new user
     *
     * @param user The user to register
     * @return Success message
     */
    public String registerUser(User user) {
        // Check if the username already exists
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        // Encrypt the user's password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save the user to the database
        userRepository.save(user);

        return "User registered successfully!";
    }

    /**
     * Login a user and return a JWT token
     *
     * @param user The user attempting to log in
     * @return JWT token
     */
    public String loginUser(User user) {
        // Find the user by username
        User existingUser = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        // Check if the provided password matches the stored password
        if (!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        // Generate a JWT token
        return generateToken(existingUser.getUsername());
    }

    /**
     * Generate a JWT token for the authenticated user
     *
     * @param username The username of the authenticated user
     * @return JWT token
     */
    private String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Token validity
                .signWith(SECRET_KEY) // Use the shared secret key
                .compact();
    }

    /**
     * Validate a JWT token and retrieve claims
     *
     * @param token The JWT token to validate
     * @return Claims extracted from the token
     */
    public Claims validateToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY) // Use the shared secret key
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new RuntimeException("Invalid or expired token: " + e.getMessage());
        }
    }

    /**
     * Extract username from a JWT token
     *
     * @param token The JWT token
     * @return The username extracted from the token
     */
    public String extractUsernameFromToken(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7); // Remove "Bearer " prefix
        }

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY) // Use the shared secret key
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject(); // Return the username (subject)
    }
    public User findUserByUsername(String username) throws Exception {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            return userOpt.get();
        } else {
            throw new Exception("Invalid Username or Password");
        }
    }

}
