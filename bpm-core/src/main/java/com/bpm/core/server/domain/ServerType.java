package com.bpm.core.server.domain;

public enum ServerType {
    REST("Rest"),
    MAIL("Mail"),
    FILE("File");

    private final String label;

    ServerType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
