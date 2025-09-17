package com.bpm.core.serviceconfig.domain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum ServiceType {
    REST, SOAP, DB, MAIL, FILE;

    public static List<String> listAll() {
        return Arrays.stream(values())
                     .map(Enum::name)
                     .collect(Collectors.toList());
    }
}
