package com.bpm.core.service;

import java.util.Map;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.bpm.core.entity.Response;
import com.bpm.core.entity.ServiceConfig;
import com.bpm.core.entity.ServiceLog;
import com.bpm.core.repository.ServiceLogRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RESTInvoker {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final ServiceLogRepository logRepository;

    public RESTInvoker(WebClient webClient, ObjectMapper objectMapper, ServiceLogRepository logRepository) {
        this.webClient = webClient;
        this.objectMapper = objectMapper;
        this.logRepository = logRepository;
    }

    public Response<String> invoke(ServiceConfig config, String payloadJson) {
    	HttpMethod httpMethod = HttpMethod.valueOf(config.getHttpMethod().toUpperCase());

        final Map<String, String> headers;
        try {
            headers = objectMapper.readValue(config.getHeaders(), new TypeReference<Map<String, String>>() {});
        } catch (Exception e) {
        	return Response.error("Invalid JSON format in headers: " + e.getMessage());
        }

        long start = System.currentTimeMillis();
        String response = null;
        int statusCode = 200;
        String errorMessage = null;
        Long logId = null;

        try {
            WebClient.RequestHeadersSpec<?> requestSpec;

            if (httpMethod == HttpMethod.GET || httpMethod == HttpMethod.DELETE) {
                requestSpec = webClient.method(httpMethod)
                        .uri(config.getTargetUrl())
                        .headers(h -> headers.forEach(h::set));
            } else {
                requestSpec = webClient.method(httpMethod)
                        .uri(config.getTargetUrl())
                        .headers(h -> headers.forEach(h::set))
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(payloadJson);
            }

            response = requestSpec
                    .retrieve()
                    .onStatus(status -> status.isError(), res ->
                        res.bodyToMono(String.class)
                           .map(body -> new RuntimeException("HTTP error: " + res.statusCode() + " - " + body))
                    )
                    .bodyToMono(String.class)
                    .block();
            
        } catch (WebClientResponseException ex) {
            statusCode = ex.getStatusCode().value();
            response = ex.getResponseBodyAsString();
            errorMessage = "HTTP error: " + statusCode + " - " + response;
            
        } catch (Exception ex) {
            statusCode = 500;
            response = ex.getMessage();
            errorMessage = "Call failed: " + response;
        } finally {
        	if (Boolean.TRUE.equals(config.getLogEnabled())) {
                ServiceLog log = new ServiceLog(
                        config.getServiceCode(),
                        payloadJson,
                        payloadJson,
                        response,
                        statusCode,
                        (int) (System.currentTimeMillis() - start)
                );
                try {
                    logId = logRepository.insertLog(log);
                } catch (Exception logEx) {
                    // Optional: log this error if needed
                }
            }
        }
        if (statusCode == 200) {
            return Response.success(response); // message="Success", data = response
        } else {
            String msgWithLog = (logId != null) ? errorMessage + " [Log_ID: " + logId + "]" : errorMessage;
            return Response.error(msgWithLog);
        }
    }
}
