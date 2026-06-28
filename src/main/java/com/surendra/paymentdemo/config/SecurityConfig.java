package com.surendra.paymentdemo.config; // config package

import org.springframework.context.annotation.Bean; // creates spring bean
import org.springframework.context.annotation.Configuration; // marks config class
import org.springframework.security.config.annotation.web.builders.HttpSecurity; // security rules
import org.springframework.security.web.SecurityFilterChain; // security filter chain

@Configuration // spring reads this class during startup
public class SecurityConfig {

    @Bean // spring stores this object
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { // configure security

        http.csrf(csrf -> csrf.disable()); // disable csrf for postman testing

        http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll()); // allow all apis for now

        return http.build(); // build security config
    }
}