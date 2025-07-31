package com.bpm.core.model.db;

public class DbOutputMapping {

    private Integer id;
    private Long dbConfigId;

    private String columnName;
    private String outputField;

    public DbOutputMapping() {}

    public DbOutputMapping(Integer id, Long dbConfigId, String columnName, String outputField) {
        this.id = id;
        this.dbConfigId = dbConfigId;
        this.columnName = columnName;
        this.outputField = outputField;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Long getDbConfigId() { return dbConfigId; }
    public void setDbConfigId(Long dbConfigId) { this.dbConfigId = dbConfigId; }

    public String getColumnName() { return columnName; }
    public void setColumnName(String columnName) { this.columnName = columnName; }

    public String getOutputField() { return outputField; }
    public void setOutputField(String outputField) { this.outputField = outputField; }
}
