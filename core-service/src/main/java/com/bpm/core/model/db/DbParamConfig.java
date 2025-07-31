package com.bpm.core.model.db;

public class DbParamConfig {

    private Integer id;
    private Long dbConfigId;

    private String paramName;
    private String paramType;    // STRING, INT, DATE...
    private String paramMode;    // IN, OUT
    private Integer paramOrder;

    public DbParamConfig() {}

    public DbParamConfig(Integer id, Long dbConfigId, String paramName, String paramType, String paramMode, Integer paramOrder) {
        this.id = id;
        this.dbConfigId = dbConfigId;
        this.paramName = paramName;
        this.paramType = paramType;
        this.paramMode = paramMode;
        this.paramOrder = paramOrder;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Long getDbConfigId() { return dbConfigId; }
    public void setDbConfigId(Long dbConfigId) { this.dbConfigId = dbConfigId; }

    public String getParamName() { return paramName; }
    public void setParamName(String paramName) { this.paramName = paramName; }

    public String getParamType() { return paramType; }
    public void setParamType(String paramType) { this.paramType = paramType; }

    public String getParamMode() { return paramMode; }
    public void setParamMode(String paramMode) { this.paramMode = paramMode; }

    public Integer getParamOrder() { return paramOrder; }
    public void setParamOrder(Integer paramOrder) { this.paramOrder = paramOrder; }
}
