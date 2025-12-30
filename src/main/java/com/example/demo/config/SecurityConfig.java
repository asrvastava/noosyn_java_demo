package com.example.demo.config;

import com.example.demo.util.ControllerUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(ControllerUtil.SIGNUP, ControllerUtil.LOGIN).permitAll()
                        .requestMatchers(HttpMethod.GET, ControllerUtil.PRODUCT, ControllerUtil.PRODUCT + "/**")
                        .hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers(ControllerUtil.PRODUCT, ControllerUtil.PRODUCT + "/**").hasAuthority("ADMIN")
                        .requestMatchers(ControllerUtil.ROLE, ControllerUtil.ROLE_CREATE).authenticated()
                        .anyRequest().permitAll())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
