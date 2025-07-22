package com.bpm.core.util;

import com.bpm.core.entity.Response;
import com.bpm.core.entity.ServiceLog;
import com.bpm.core.repository.ServiceLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ErrorUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static ServiceLogRepository logRepository;

    public static void setLogRepository(ServiceLogRepository repo) {
        logRepository = repo;
    }

    public static String generateErrorJson(String message, String serviceCode, String requestData, int statusCode, long startTime) {
        long duration = System.currentTimeMillis() - startTime;
        String responseJson;

        try {
            responseJson = objectMapper.writeValueAsString(Response.error(1, message));
        } catch (Exception e) {
            responseJson = "{\"code\":500,\"message\":\"Unexpected error\",\"data\":null}";
        }

        Long logId = logExecution(serviceCode, requestData, responseJson, statusCode, duration);

        if (logId != null) {
            String enrichedMessage = String.format("%s [Log_ID: %d]", message, logId);
            try {
                return objectMapper.writeValueAsString(Response.error(1, enrichedMessage));
            } catch (Exception ex2) {
                return String.format("{\"code\":1,\"message\":\"%s\",\"data\":null}", escapeJson(enrichedMessage));
            }
        }

        return responseJson;
    }

    private static Long logExecution(String serviceCode, String requestData, String responseData, int statusCode, long duration) {
        if (logRepository == null) return null;

        ServiceLog log = new ServiceLog(
            serviceCode,
            null,
            requestData,
            responseData,
            statusCode,
            (int) duration
        );
        try {
            return logRepository.insertLog(log);
        } catch (Exception e) {
            System.err.println("Failed to insert log: " + e.getMessage());
            return null;
        }
    }

    private static String escapeJson(String s) {
        return s.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }
}
