package com.bpm.core.server.domain;

public enum ServerType {
    REST("REST Server"),
    MAIL("MAIL Server");

    private final String label;

    ServerType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
