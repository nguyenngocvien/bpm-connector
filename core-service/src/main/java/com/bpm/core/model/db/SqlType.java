package com.bpm.core.model.db;

public enum SqlType {
    QUERY("Query"),
    UPDATE("Update"),
    INSERT("Insert"),
    DELETE("Delete"),
    STORED_PROC("Stored Procedure");

    private final String label;

    SqlType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
