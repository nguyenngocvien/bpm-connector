package com.bpm.core.service;

import java.util.Map;

import com.bpm.core.entity.Response;
import com.bpm.core.entity.ServiceConfig;
import com.bpm.core.entity.ServiceLog;
import com.bpm.core.repository.ServiceLogRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RESTInvoker {

    private final ObjectMapper objectMapper;
    private final ServiceLogRepository logRepository;

    public RESTInvoker(ObjectMapper objectMapper, ServiceLogRepository logRepository) {
        this.objectMapper = objectMapper;
        this.logRepository = logRepository;
    }

    public Response<String> invoke(ServiceConfig config, String payloadJson) {
        String httpMethod = config.getHttpMethod().toUpperCase();
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
            URL url = new URL(targetUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(httpMethod);

            // Add headers
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }

            if (httpMethod.equals("POST") || httpMethod.equals("PUT") || httpMethod.equals("PATCH")) {
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                try (OutputStream os = connection.getOutputStream()) {
                    os.write(payloadJson.getBytes("UTF-8"));
                }
            }

            statusCode = connection.getResponseCode();
            InputStream inputStream = (statusCode >= 200 && statusCode < 300)
                    ? connection.getInputStream()
                    : connection.getErrorStream();

            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder responseBuilder = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                responseBuilder.append(line);
            }
            in.close();
            responseBody = responseBuilder.toString();

            if (statusCode >= 400) {
                errorMessage = "HTTP error: " + statusCode + " - " + responseBody;
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
                    // Optional: log this error if needed
                }
            }
        }

        if (statusCode == 200) {
            return Response.success(responseBody);
        } else {
            String msgWithLog = (logId != null) ? errorMessage + " [Log_ID: " + logId + "]" : errorMessage;
            return Response.error(msgWithLog);
        }
    }
}


//public class RESTInvoker {
//
//    private final HttpClient httpClient;
//    private final ObjectMapper objectMapper;
//    private final ServiceLogRepository logRepository;
//
//    public RESTInvoker(ObjectMapper objectMapper, ServiceLogRepository logRepository) {
//        this.httpClient = HttpClient.newBuilder().build();
//        this.objectMapper = objectMapper;
//        this.logRepository = logRepository;
//    }
//
//    public Response<String> invoke(ServiceConfig config, String payloadJson) {
//        String method = config.getHttpMethod().toUpperCase();
//        Map<String, String> headers;
//
//        try {
//            headers = objectMapper.readValue(config.getHeaders(), new TypeReference<>() {});
//        } catch (Exception e) {
//            return Response.error("Invalid JSON format in headers: " + e.getMessage());
//        }
//
//        long start = System.currentTimeMillis();
//        String response = null;
//        int statusCode = 200;
//        String errorMessage = null;
//        Long logId = null;
//
//        try {
//            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
//                    .uri(URI.create(config.getTargetUrl()));
//
//            headers.forEach(requestBuilder::header);
//
//            if (method.equals("GET") || method.equals("DELETE")) {
//                requestBuilder.method(method, HttpRequest.BodyPublishers.noBody());
//            } else {
//                requestBuilder
//                        .header("Content-Type", "application/json")
//                        .method(method, HttpRequest.BodyPublishers.ofString(payloadJson));
//            }
//
//            HttpRequest request = requestBuilder.build();
//            HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//
//            statusCode = httpResponse.statusCode();
//            response = httpResponse.body();
//
//            if (statusCode >= 400) {
//                errorMessage = "HTTP error: " + statusCode + " - " + response;
//            }
//
//        } catch (IOException | InterruptedException e) {
//            statusCode = 500;
//            response = e.getMessage();
//            errorMessage = "Call failed: " + response;
//        } finally {
//            if (Boolean.TRUE.equals(config.getLogEnabled())) {
//                ServiceLog log = new ServiceLog(
//                        config.getServiceCode(),
//                        payloadJson,
//                        payloadJson,
//                        response,
//                        statusCode,
//                        (int) (System.currentTimeMillis() - start)
//                );
//                try {
//                    logId = logRepository.insertLog(log);
//                } catch (Exception logEx) {
//                    // Optional: Log error
//                }
//            }
//        }
//
//        if (statusCode == 200) {
//            return Response.success(response);
//        } else {
//            String msgWithLog = (logId != null) ? errorMessage + " [Log_ID: " + logId + "]" : errorMessage;
//            return Response.error(msgWithLog);
//        }
//    }
//}
