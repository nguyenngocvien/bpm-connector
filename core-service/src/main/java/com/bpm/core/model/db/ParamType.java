package com.bpm.core.model.db;

import java.sql.Types;

public enum ParamType {
    VARCHAR("VARCHAR", Types.VARCHAR),
    TEXT("TEXT", Types.VARCHAR),
    INTEGER("INTEGER", Types.INTEGER),
    BIGINT("BIGINT", Types.BIGINT),
    SMALLINT("SMALLINT", Types.SMALLINT),
    NUMERIC("NUMERIC", Types.NUMERIC),
    DECIMAL("DECIMAL", Types.NUMERIC),
    DOUBLE("DOUBLE PRECISION", Types.DOUBLE),
    REAL("REAL", Types.REAL),
    BOOLEAN("BOOLEAN", Types.BOOLEAN),
    DATE("DATE", Types.DATE),
    TIMESTAMP("TIMESTAMP", Types.TIMESTAMP),
    BYTEA("BYTEA", Types.BINARY);

    private final String label;
    private final int sqlType;

    ParamType(String label, int sqlType) {
        this.label = label;
        this.sqlType = sqlType;
    }

    public String getLabel() {
        return label;
    }

    public int getSqlType() {
        return sqlType;
    }

    public static ParamType fromString(String value) {
        if (value == null) return null;
        for (ParamType type : values()) {
            if (type.name().equalsIgnoreCase(value) || type.label.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown ParamType: " + value);
    }
}
