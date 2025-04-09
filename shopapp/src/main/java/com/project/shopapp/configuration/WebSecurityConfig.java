package com.project.shopapp.configuration;

import com.project.shopapp.filters.JwtTokenFilter;
import com.project.shopapp.models.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

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
        http.cors(new Customizer<CorsConfigurer<HttpSecurity>>() {
            @Override
            public void customize(CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(List.of("*"));
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
                configuration.setExposedHeaders(List.of("x-auth-token"));

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);

                httpSecurityCorsConfigurer.configurationSource(source);
            }
        });

        return http.build();
    }
}
