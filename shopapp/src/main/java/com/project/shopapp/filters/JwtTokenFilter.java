package com.project.shopapp.filters;

import com.project.shopapp.component.JwtTokenUtil;
import com.project.shopapp.models.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
         try {
             if(isBypassToken(request)){
                 filterChain.doFilter(request,response);
                 return;
             }
             final String authHeader = request.getHeader("Authorization");
             if (authHeader != null && authHeader.startsWith("Bearer ")) {
                 final String token = authHeader.substring(7);
                 final String phoneNumber = jwtTokenUtil.extractPhoneNumber(token);
                 if(phoneNumber != null && SecurityContextHolder.getContext().getAuthentication() ==  null){
                     User userDetails =(User) userDetailsService.loadUserByUsername(phoneNumber);
                     if(jwtTokenUtil.validateToken(token,userDetails)){
                         UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                                 userDetails,
                                 null,
                                 userDetails.getAuthorities());
                         usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                         SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                     }
                 }
                 filterChain.doFilter(request,response);
             }
             else{
                 response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Unauthoried");
                return;
             }
         }catch (Exception e){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Unauthoried");
         }


    }
    private boolean isBypassToken(HttpServletRequest request) {
        final List<Pair<String, String>> bypassTokens = Arrays.asList(
//                Pair.of("api/v1/products", "GET"),
                Pair.of("api/v1/roles", "GET"),
                Pair.of("api/v1/categories", "GET"),
                Pair.of("api/v1/users/register", "POST"),
                Pair.of("api/v1/users/login", "POST")
        );

        for (Pair<String, String> bypassToken: bypassTokens) {
            if (request.getServletPath().contains(bypassToken.getFirst()) &&
                    request.getMethod().equals(bypassToken.getSecond())) {
                return true;
            }
        }
        return false;
    }

}
