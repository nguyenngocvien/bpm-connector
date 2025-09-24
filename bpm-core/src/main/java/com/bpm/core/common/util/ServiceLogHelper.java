package com.bpm.core.common.util;

import com.bpm.core.serviceconfig.domain.ServiceConfig;
import com.bpm.core.servicelog.domain.ServiceLog;
import com.bpm.core.servicelog.service.ServiceLogService;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class ServiceLogHelper {
    private final ServiceLogService logService;

    public Long save(ServiceConfig config,
                     String rawParams,
                     String mappedRequest,
                     String responseData,
                     int statusCode,
                     int durationMs,
                     long startTime) {
        try {
            Map<String, Object> inputMap = JsonUtil.toObjectMap(rawParams);
            ServiceLog log = ServiceLogHelper.buildLog(
                    config, inputMap, mappedRequest, responseData, statusCode, startTime
            );
            log.setDurationMs(durationMs);
            return logService.createLog(log);
        } catch (Exception e) {
            System.err.println("Failed to save log: " + e.getMessage());
            return null;
        }
    }

    public static ServiceLog buildLog(ServiceConfig config,
                                      Map<String, Object> inputParams,
                                      String mappedRequest,
                                      String response,
                                      int statusCode,
                                      long startTime) {
        ServiceLog log = new ServiceLog();
        log.setServiceCode(config.getServiceCode());
        log.setRequestData(JsonUtil.toString(inputParams));
        log.setMappedRequest(mappedRequest);
        log.setResponseData(response);
        log.setStatusCode(statusCode);
        log.setCreatedAt(java.time.LocalDateTime.now());
        log.setDurationMs((int) (System.currentTimeMillis() - startTime));
        return log;
    }
}
