package com.bpm.core.db.domain;

import java.sql.Types;

public enum ParamType {
    STRING(Types.VARCHAR),
    INT(Types.INTEGER),
    LONG(Types.BIGINT),
    DATE(Types.DATE),
    TIMESTAMP(Types.TIMESTAMP),
    BOOLEAN(Types.BOOLEAN),
    DOUBLE(Types.DOUBLE);

    private final int sqlType;

    ParamType(int sqlType) {
        this.sqlType = sqlType;
    }

    public int getSqlType() {
        return sqlType;
    }

    public static ParamType fromString(String type) {
        try {
            return ParamType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            return STRING; // default
        }
    }
}
