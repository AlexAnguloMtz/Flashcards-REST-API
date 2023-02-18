package com.aram.auth.security;

import com.aram.auth.service.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfiguration {

    @Bean
    public SecurityFilterChain configure(
            HttpSecurity http,
            JwtFilter jwtFilter,
            RestAuthenticationEntryPoint authenticationEntryPoint
    ) throws Exception {

        http.csrf().disable();

        http.sessionManagement(session -> session.sessionCreationPolicy(STATELESS));

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling(auth -> auth.authenticationEntryPoint(authenticationEntryPoint));

        http.authorizeHttpRequests()
                .requestMatchers("/users/signup", "/users/login").permitAll()
                .anyRequest().authenticated();

        return http.build();
    }

}