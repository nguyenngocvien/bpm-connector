package com.bpm.api.service;

import org.springframework.stereotype.Service;

import com.bpm.core.service.SQLConnector;

@Service
public class SQLService {

    private final SQLConnector sqlConnector;

    public SQLService(SQLConnector sqlConnector) {
        this.sqlConnector = sqlConnector;
    }

    public String handleQuery(String jsonInput) {
        return sqlConnector.executeQuery(jsonInput);
    }

    public String handleUpdate(String jsonInput) {
        return sqlConnector.executeUpdate(jsonInput);
    }

    public String handleProcedure(String jsonInput) {
        return sqlConnector.executeProcedure(jsonInput);
    }
}
