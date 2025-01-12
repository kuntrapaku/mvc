package com.example.mvc.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Replace with actual user lookup logic (e.g., database query)
        if ("testuser".equals(username)) {
            return new User("testuser", "password", Collections.emptyList());
        }
        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}
