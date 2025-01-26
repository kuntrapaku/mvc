package com.example.mvc.service;

import com.example.mvc.repository.UserRepository;
import com.example.mvc.model.User; // Ensure this is the correct import for your User entity
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {


    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Load a user by username and return a Spring Security UserDetails object.
     *
     * @param username the username to search for
     * @return the UserDetails object
     * @throws UsernameNotFoundException if no user is found with the given username
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Looking for user with username: " + username); // Debug log

        // Fetch the user from the repository
        return userRepository.findByUsername(username)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getUsername(),
                        user.getPassword(),
                        Collections.emptyList() // Replace with roles/authorities if applicable
                ))
                .orElseThrow(() -> {
                    System.err.println("User not found with username: " + username); // Debug log
                    return new UsernameNotFoundException("User not found with username: " + username);
                });
    }
}
