package com.bpm.core.auth.domain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum AuthType {
    BASIC("Basic Authentication"),
    BEARER("Bearer Token"),
    API_KEY("API Key"),
    OAUTH2("OAuth 2.0"),
    NONE("No Authentication");

    private final String label;

    AuthType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static List<String> listAll() {
        return Arrays.stream(values())
                     .map(Enum::name)
                     .collect(Collectors.toList());
    }
}
