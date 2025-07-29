package com.bpm.api.service;

import com.bpm.core.entity.SQLRequest;
import com.bpm.core.service.SQLConnector;
import com.bpm.api.model.SQLProcRequestPayload;
import com.bpm.api.model.SQLRequestPayload;
import com.bpm.core.entity.Response;
import com.bpm.core.entity.SQLProcedureRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;

@Service
public class SQLService {

    private final SQLConnector sqlConnector;
    private final ObjectMapper objectMapper;

    public SQLService(SQLConnector sqlConnector, ObjectMapper objectMapper) {
        this.sqlConnector = sqlConnector;
        this.objectMapper = objectMapper;
    }

    public String executeQuery(SQLRequestPayload payload) {
        try {
            SQLRequest req = new SQLRequest(payload.getDatasource(), payload.getSql(), payload.getParams());
            Response<?> response = sqlConnector.executeQuery(req);
            return objectMapper.writeValueAsString(response);
        } catch (Exception e) {
            return buildErrorJson(e);
        }
    }

    public String executeUpdate(SQLRequestPayload payload) {
        try {
            SQLRequest req = new SQLRequest(payload.getDatasource(), payload.getSql(), payload.getParams());
            Response<?> response = sqlConnector.executeUpdate(req);
            return objectMapper.writeValueAsString(response);
        } catch (Exception e) {
            return buildErrorJson(e);
        }
    }

    public String executeProcedure(SQLProcRequestPayload payload) {
        try {
            SQLProcedureRequest req = new SQLProcedureRequest(payload.getDatasource(),
                    payload.getName(),
                    payload.getNamedInParams(),
                    payload.getOutParams());
            Response<?> response = sqlConnector.executeProcedure(req);
            return objectMapper.writeValueAsString(response);
        } catch (Exception e) {
            return buildErrorJson(e);
        }
    }

    private String buildErrorJson(Exception e) {
        try {
            e.printStackTrace();
            Response<Object> err = Response.error(1, "Error: " + e.getMessage());
            return objectMapper.writeValueAsString(err);
        } catch (Exception ex) {
            return "{\"code\":1,\"message\":\"Error\",\"data\":null}";
        }
    }
}