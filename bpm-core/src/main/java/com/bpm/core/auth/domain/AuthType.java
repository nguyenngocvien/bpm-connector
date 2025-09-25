package com.bpm.core.auth.domain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum AuthType {
	NONE("No Authentication"),
    BASIC("Basic Authentication"),
    BEARER("Bearer Token"),
    API_KEY("API Key"),
    OAUTH2("OAuth 2.0");

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
