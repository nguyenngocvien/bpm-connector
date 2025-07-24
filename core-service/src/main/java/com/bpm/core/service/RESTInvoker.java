package com.bpm.core.service;

import java.time.Duration;
import java.util.Map;

import com.bpm.core.entity.Response;
import com.bpm.core.entity.ServiceConfig;
import com.bpm.core.entity.ServiceLog;
import com.bpm.core.repository.ServiceLogRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

@Service
public class RESTInvoker {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final ServiceLogRepository logRepository;

    public RESTInvoker(WebClient.Builder webClientBuilder,
                       ObjectMapper objectMapper,
                       ServiceLogRepository logRepository) {
        this.webClient = webClientBuilder.build(); // WebClient từ Spring context
        this.objectMapper = objectMapper;
        this.logRepository = logRepository;
    }

    public Response<String> invoke(ServiceConfig config, String payloadJson) {
        String methodStr = config.getHttpMethod().toUpperCase();
        HttpMethod httpMethod;
        String targetUrl = config.getTargetUrl();
        Map<String, String> headers;

        try {
        	headers = objectMapper.readValue(config.getHeaders(), new TypeReference<Map<String, String>>() {});
        } catch (Exception e) {
            return Response.error("Invalid JSON format in headers: " + e.getMessage());
        }

        long start = System.currentTimeMillis();
        String responseBody = null;
        int statusCode = 200;
        String errorMessage = null;
        Long logId = null;

        try {
            httpMethod = HttpMethod.valueOf(methodStr);
        } catch (IllegalArgumentException ex) {
            return Response.error("Unsupported HTTP method: " + methodStr);
        }

        try {
            WebClient.RequestBodySpec requestSpec = webClient.method(httpMethod)
                    .uri(targetUrl)
                    .headers(httpHeaders -> headers.forEach(httpHeaders::set));

            Mono<ClientResponse> responseMono;
            if (httpMethod == HttpMethod.POST || httpMethod == HttpMethod.PUT || httpMethod == HttpMethod.PATCH) {
                requestSpec.contentType(MediaType.APPLICATION_JSON);
                responseMono = requestSpec.body(BodyInserters.fromValue(payloadJson))
                        .exchangeToMono(Mono::just);
            } else {
                responseMono = requestSpec.exchangeToMono(Mono::just);
            }

            ClientResponse response = responseMono
                    .block(Duration.ofSeconds(10));

            if (response == null) {
                statusCode = 500;
                errorMessage = "No response received";
                responseBody = errorMessage;
            } else {
                statusCode = response.statusCode().value();
                responseBody = response.bodyToMono(String.class).block();
                if (response.statusCode().isError()) {
                    errorMessage = "HTTP error: " + statusCode + " - " + responseBody;
                }
            }

        } catch (Exception ex) {
            statusCode = 500;
            responseBody = ex.getMessage();
            errorMessage = "Call failed: " + responseBody;
        } finally {
            if (Boolean.TRUE.equals(config.getLogEnabled())) {
                ServiceLog log = new ServiceLog(
                        config.getServiceCode(),
                        payloadJson,
                        payloadJson,
                        responseBody,
                        statusCode,
                        (int) (System.currentTimeMillis() - start)
                );
                try {
                    logId = logRepository.insertLog(log);
                } catch (Exception logEx) {
                    // Optional: log error
                }
            }
        }

        if (statusCode >= 200 && statusCode < 300) {
            return Response.success(responseBody);
        } else {
            String msgWithLog = (logId != null) ? errorMessage + " [Log_ID: " + logId + "]" : errorMessage;
            return Response.error(msgWithLog);
        }
    }
}
