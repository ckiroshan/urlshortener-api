package com.ckiroshan.urlshortener.config;

import com.ckiroshan.urlshortener.repository.UserRepository;
import com.ckiroshan.urlshortener.service.impl.JwtServiceImpl;
import com.ckiroshan.urlshortener.service.impl.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserRepository userRepository;
    private final RateLimitingFilter rateLimitingFilter;

    @Bean
    // Main security filter chain configuration
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // Disable CSRF for stateless API
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints (auth, URL redirect, error)
                        .requestMatchers("/api/auth/**", "/{shortCode}", "/error").permitAll()
                        // All other endpoints require authentication
                        .anyRequest().authenticated())
                .sessionManagement(session -> session
                        // Enforce stateless session (JWT-based auth)
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                // Apply rate limiting filter before User authentication
                .addFilterBefore(rateLimitingFilter, BasicAuthenticationFilter.class)
                // Add JWT filter before the default UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    // JWT authentication filter bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtService(), userDetailsService());
    }

    @Bean
    // Configures authentication to use custom user details + password encoder
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    // Custom UserDetailsService bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl(userRepository);
    }

    @Bean
    // JwtService implementation bean
    public JwtServiceImpl jwtService() {
        return new JwtServiceImpl();
    }

    @Bean
    // BCrypt password encoder bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}