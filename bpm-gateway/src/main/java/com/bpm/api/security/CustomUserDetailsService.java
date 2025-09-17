package com.bpm.api.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bpm.core.auth.domain.AuthConfig;
import com.bpm.core.auth.repository.AuthRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AuthRepository authUserRepository;

    public CustomUserDetailsService(AuthRepository authUserRepository) {
        this.authUserRepository = authUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthConfig user = authUserRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Transfer AuthUser to UserDetails
        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }
}
