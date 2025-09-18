package com.bpm.core.db.domain;

public enum DbType {
    POSTGRES, MYSQL, ORACLE, SQLSERVER, H2, UNKNOWN;

    public static DbType detect(String jdbcUrl) {
        if (jdbcUrl == null) return UNKNOWN;
        if (jdbcUrl.startsWith("jdbc:postgresql:")) return POSTGRES;
        if (jdbcUrl.startsWith("jdbc:mysql:")) return MYSQL;
        if (jdbcUrl.startsWith("jdbc:oracle:")) return ORACLE;
        if (jdbcUrl.startsWith("jdbc:sqlserver:")) return SQLSERVER;
        if (jdbcUrl.startsWith("jdbc:h2:")) return H2;
        return UNKNOWN;
    }
}
