package com.bpm.api.security;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.bpm.core.auth.domain.AuthResult;
import com.bpm.core.auth.service.AuthManager;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthFilter extends OncePerRequestFilter {

    @Autowired
    private AuthManager authManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        if (isPublicPath(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        AuthResult result = authManager.authenticate(authHeader);

        if (result.isSuccess()) {
            // Assign ROLE_USER authority by default
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            result.getUsername(), null,
                            Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))
                    );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Unauthorized: " + result.getError() + "\"}");
        }
    }

    private boolean isPublicPath(String path) {
        return path.equals("/login") 
        		|| path.startsWith("/admin") 
        		|| path.startsWith("/css/") 
        		|| path.startsWith("/js/") 
        		|| path.contains("/swagger-ui")
        		|| path.contains("/v3/api-docs")
        		|| path.contains("/swagger-resources")
        		|| path.contains("/webjars")
        		|| path.contains("/configuration");
    }
}