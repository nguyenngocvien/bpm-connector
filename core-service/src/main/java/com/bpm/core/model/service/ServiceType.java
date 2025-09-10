package com.bpm.core.model.service;

import java.util.Arrays;
import java.util.List;

public enum ServiceType {
    REST("REST API"),
    SOAP("SOAP Web Service"),
    DB("Database"),
    MAIL("Mail Service"),
    FILE("File Storage");

    private final String label;

    ServiceType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static List<ServiceType> listAll() {
        return Arrays.asList(values());
    }
}
