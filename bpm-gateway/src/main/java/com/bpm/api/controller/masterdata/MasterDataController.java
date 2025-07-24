package com.bpm.api.controller.masterdata;

import org.springframework.stereotype.Service;

import com.bpm.core.service.SQLConnector;

@Service
public class MasterDataController {

    private final SQLConnector sqlConnector;

    public MasterDataController(SQLConnector sqlConnector) {
        this.sqlConnector = sqlConnector;
    }

    public String handleQuery(String jsonInput) {
        return sqlConnector.executeQuery(jsonInput);
    }
}
