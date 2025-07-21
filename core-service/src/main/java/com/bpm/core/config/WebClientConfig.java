package com.bpm.core.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {

	@Bean
	public WebClient webClient(WebClient.Builder builder) {
	    return builder
	        .baseUrl("") // optional
	        .clientConnector(new ReactorClientHttpConnector(
	            HttpClient.create()
	                      .responseTimeout(Duration.ofSeconds(10))
	        ))
	        .build();
	}
}
