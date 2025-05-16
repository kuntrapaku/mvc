package com.example.mvc.config;

import com.example.mvc.security.CustomJwtFilter;
import com.example.mvc.service.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;
    private final AccessDeniedHandler customAccessDeniedHandler;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider,
                          UserDetailsService userDetailsService,
                          AccessDeniedHandler customAccessDeniedHandler) {
        this.jwtTokenProvider      = jwtTokenProvider;
        this.userDetailsService    = userDetailsService;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }

    /** ----------------------------------------------------------------
     *  MAIN SECURITY FILTER CHAIN
     *  ---------------------------------------------------------------- */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))   // delegate to same config
                .authorizeHttpRequests(auth -> auth
                        // 🟢 Public endpoints  – list FIRST (order matters)
                        .requestMatchers(
                                "/api/chat/ask",
                                "/api/chat/health",
                                "/api/auth/register",
                                "/api/auth/login",
                                "/h2-console/**"
                        ).permitAll()
                        // 🔒 Everything else requires authentication
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(customAccessDeniedHandler))
                .addFilterBefore(new CustomJwtFilter(jwtTokenProvider, userDetailsService),
                        UsernamePasswordAuthenticationFilter.class)
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable());

        return http.build();
    }

    /** ----------------------------------------------------------------
     *  PASSWORD ENCODER & AUTH MANAGER
     *  ---------------------------------------------------------------- */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /** ----------------------------------------------------------------
     *  GLOBAL CORS CONFIGURATION (used by .cors() above)
     *  ---------------------------------------------------------------- */
    private UrlBasedCorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));        // wildcard allowed with credentials
        config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);                     // cookies / auth headers allowed

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    /** ----------------------------------------------------------------
     *  EXPLICIT CORS FILTER at HIGHEST precedence.
     *  Guarantees CORS runs BEFORE security filters.
     *  ---------------------------------------------------------------- */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public CorsFilter corsFilter() {
        return new CorsFilter(corsConfigurationSource());
    }
}
