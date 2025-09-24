package com.bpm.core.serviceconfig.service.impl;

import com.bpm.core.common.exception.ServiceExecutionException;
import com.bpm.core.common.response.Response;
import com.bpm.core.common.util.JsonUtil;
import com.bpm.core.common.util.ServiceLogHelper;
import com.bpm.core.db.infrastructure.DbExecutorHelper;
import com.bpm.core.serviceconfig.domain.ServiceConfig;
import com.bpm.core.serviceconfig.domain.ServiceType;
import com.bpm.core.serviceconfig.interfaces.ServiceInvoker;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.*;

@Slf4j
public class DbExecutor implements ServiceInvoker {
    private final DbExecutorHelper dbHelper;
    private final ServiceLogHelper logHelper;
    private final TransactionTemplate txTemplate;

    public DbExecutor(DbExecutorHelper dbHelper,
                      ServiceLogHelper logHelper,
                      TransactionTemplate txTemplate) {
        this.dbHelper = dbHelper;
        this.logHelper = logHelper;
        this.txTemplate = txTemplate;
    }

    @Override
    public ServiceType getServiceType() {
        return ServiceType.DB;
    }

    @Override
    public Response execute(ServiceConfig config, String params) {
        long start = System.currentTimeMillis();
        try {
            Map<String, Object> reqMap = JsonUtil.toObjectMap(params);

            Object result = txTemplate.execute(status -> dbHelper.execute(config, reqMap));

            int duration = (int) (System.currentTimeMillis() - start);
            if (Boolean.TRUE.equals(config.getLogEnabled())) {
                logHelper.save(config, params, null,
                        result != null ? result.toString() : null,
                        HttpStatus.OK.value(),
                        duration,
                        start);
            }

            return Response.success(JsonUtil.toString(result));
        } catch (ServiceExecutionException e) {
            handleError(config, params, e.getMessage(), start, e.getStatusCode());
            return Response.error(e.getCode(), e.getMessage(), e.getLogId().toString());
        } catch (Exception e) {
            handleError(config, params, e.getMessage(), start, HttpStatus.INTERNAL_SERVER_ERROR.value());
            return Response.error("DB_ERROR", e.getMessage());
        }
    }

    private void handleError(ServiceConfig config, String rawParams, String errorMsg,
                             long start, int statusCode) {
        try {
            if (Boolean.TRUE.equals(config.getLogEnabled())) {
                int duration = (int) (System.currentTimeMillis() - start);
                logHelper.save(config, rawParams, null,
                        errorMsg, statusCode, duration, start);
            }
        } catch (Exception logEx) {
            log.error("Failed to save log: {}", logEx.getMessage(), logEx);
        }
    }
}