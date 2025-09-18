package com.bpm.core.serviceconfig.domain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum ServiceType {

	SQL("SQL"),
	REST("REST"),
    MAIL("MAIL"),
    FILE("FILE");

    public static List<String> listAll() {
        return Arrays.stream(values())
                     .map(Enum::name)
                     .collect(Collectors.toList());
    }

    private final String label;

    ServiceType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

