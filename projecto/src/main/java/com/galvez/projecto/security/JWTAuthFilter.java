package com.galvez.projecto.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.galvez.projecto.utils.JWTUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTAuthFilter extends OncePerRequestFilter{
    
    @Autowired
    private JWTUtils jwtUtils;

    // @Autowired
    // private CustomerUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        String jwtToken;
        String userEmail;

        if (authHeader == null || authHeader.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        // if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwtToken = authHeader.substring(7);
            userEmail = jwtUtils.extractUsername(jwtToken);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = jwtUtils.getUserDetailsFromToken(jwtToken);
                if (userDetails != null && jwtUtils.validateToken(jwtToken, userDetails)) {
                    // UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    // SecurityContextHolder.getContext().setAuthentication(authentication);

                    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(token);
                    
                }
            }
        // }

        filterChain.doFilter(request, response);
    }
    
    // @Override
    // protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
    //                                 FilterChain filterChain) throws ServletException, IOException {

    //     final String authHeader = request.getHeader("Authorization");
    //     final String jwtToken;
    //     final String userEmail;
    //     if (authHeader == null || authHeader.isBlank()) {
    //         filterChain.doFilter(request, response);
    //         return;
    //     }

    //     jwtToken = authHeader.substring(7);
    //     // userEmail = jwtUtils.extractUsername(jwtToken);

    //     // if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
    //     //     UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEmail);
    //     //     if (jwtUtils.isValidToken(jwtToken, userDetails)) {
    //     //         SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    //     //         UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    //     //         token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    //     //         securityContext.setAuthentication(token);
    //     //         SecurityContextHolder.setContext(securityContext);
    //     //     }
    //     // }

    //     filterChain.doFilter(request, response);
    // }
}
