package com.project.shopapp.configuration;

import com.project.shopapp.filters.JwtTokenFilter;
import com.project.shopapp.models.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests ->{
                    requests.requestMatchers(
                           "api/v1/users/register",
                           "api/v1/users/login").permitAll()
                    .requestMatchers(GET, "api/v1/orders/**").hasAnyRole("ADMIN","USER")
                    .requestMatchers(PUT, "api/v1/orders/**").hasRole("ADMIN")
                    .requestMatchers(POST, "api/v1/orders/**").hasAnyRole(Role.USER)
                    .requestMatchers(DELETE, "api/v1/orders/**").hasRole("ADMIN")

                    .requestMatchers(GET, "api/v1/categories/**").hasAnyRole("ADMIN","USER")
                    .requestMatchers(PUT, "api/v1/categories/**").hasRole("ADMIN")
                    .requestMatchers(POST, "api/v1/categories/**").hasAnyRole(Role.ADMIN)
                    .requestMatchers(DELETE, "api/v1/categories/**").hasRole("ADMIN")

                    .requestMatchers(GET, "api/v1/products/**").hasAnyRole("ADMIN","USER")
                    .requestMatchers(PUT, "api/v1/products/**").hasRole("ADMIN")
                    .requestMatchers(POST, "api/v1/products/**").hasAnyRole(Role.ADMIN)
                    .requestMatchers(DELETE, "api/v1/products/**").hasRole("ADMIN")

                    .requestMatchers(GET, "api/v1/order_details/**").hasAnyRole("ADMIN","USER")
                    .requestMatchers(PUT, "api/v1/order_details/**").hasRole("ADMIN")
                    .requestMatchers(POST, "api/v1/order_details/**").hasAnyRole(Role.USER)
                    .requestMatchers(DELETE, "api/v1/order_details/**").hasRole("ADMIN")
                    .anyRequest().authenticated();

                });
        return http.build();
    }
}
