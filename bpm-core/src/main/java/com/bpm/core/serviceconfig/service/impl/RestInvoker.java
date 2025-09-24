package com.bpm.core.serviceconfig.service.impl;

import com.bpm.core.common.exception.ServiceExecutionException;
import com.bpm.core.common.response.Response;
import com.bpm.core.common.util.ServiceLogHelper;
import com.bpm.core.rest.infrastructure.RestClientHelper;
import com.bpm.core.serviceconfig.domain.ServiceConfig;
import com.bpm.core.serviceconfig.domain.ServiceType;
import com.bpm.core.serviceconfig.interfaces.ServiceInvoker;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RestInvoker implements ServiceInvoker {
    private final RestClientHelper clientHelper;
    private final ServiceLogHelper logHelper;

    public RestInvoker(RestClientHelper clientHelper, ServiceLogHelper logHelper) {
        this.clientHelper = clientHelper;
        this.logHelper = logHelper;
    }

    @Override
    public ServiceType getServiceType() {
        return ServiceType.REST;
    }

    @Override
    public Response execute(ServiceConfig config, String params) {
        long start = System.currentTimeMillis();
        Long logId = null;

        try {
            String mappedRequest = clientHelper.mapRequest(config, params);
            String response = clientHelper.invoke(config, mappedRequest);
            long duration = System.currentTimeMillis() - start;

            if (Boolean.TRUE.equals(config.getLogEnabled())) {
                logId = logHelper.save(config, params, mappedRequest, response, 200, (int) duration, start);
            }
            return Response.success(response);

        } catch (Exception ex) {
            long duration = System.currentTimeMillis() - start;
            String errMsg = ex.getMessage();

            if (Boolean.TRUE.equals(config.getLogEnabled())) {
                logId = logHelper.save(config, params, null, errMsg, 500, (int) duration, start);
            }

            throw new ServiceExecutionException(500, "REST_ERROR", errMsg, logId);
        }
    }
}
