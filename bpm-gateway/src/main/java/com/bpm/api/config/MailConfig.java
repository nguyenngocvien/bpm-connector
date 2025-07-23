package com.bpm.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bpm.core.service.EmailService;

@Configuration
public class MailConfig {

    @Value("${mail.username}")
    private String username;

    @Value("${mail.password}")
    private String password;

    @Value("${mail.host}")
    private String host;

    @Value("${mail.port}")
    private int port;

    @Bean
    public EmailService emailService() {
        return new EmailService(username, password, host, port);
    }
}