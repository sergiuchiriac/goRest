package com.rest.goRest.security;

import com.rest.goRest.service.impl.ApiKeyService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@AllArgsConstructor
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    private final ApiKeyService apiKeyService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        var apiKey = request.getHeader("Authorization");
        if (apiKeyService.isValidApiKey(apiKey)) {
            Authentication auth = new ApiKeyAuthToken(apiKey);
            SecurityContextHolder.getContext().setAuthentication(auth);
        } else {
            throw new BadCredentialsException("Invalid API Key");
        }

        filterChain.doFilter(request, response);
    }
}
