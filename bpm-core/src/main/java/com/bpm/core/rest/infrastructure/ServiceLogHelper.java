package com.bpm.core.rest.infrastructure;

import com.bpm.core.serviceconfig.domain.ServiceConfig;
import com.bpm.core.servicelog.domain.ServiceLog;

import java.util.Map;

public class ServiceLogHelper {

    public static ServiceLog buildLog(ServiceConfig serviceConfig, Map<String, Object> inputParams,
                                      String bodyStr, String responseStr, int statusCode, long startTime) {
        ServiceLog log = new ServiceLog();
        log.setServiceCode(serviceConfig.getServiceCode());
        log.setRequestData(inputParams.toString());
        log.setMappedRequest(bodyStr);
        log.setResponseData(responseStr);
        log.setStatusCode(statusCode);
        log.setDurationMs((int) (System.currentTimeMillis() - startTime));
        return log;
    }
}
