package com.bpm.core.util;

import com.bpm.core.model.rest.NameValuePair;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;

public class RestServiceConfigParser {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Parse JSON string to List<NameValuePair>
    public static List<NameValuePair> parseNameValueList(String json) {
        if (json == null || json.trim().isEmpty()) {
            return Collections.emptyList();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<List<NameValuePair>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON to List<NameValuePair>", e);
        }
    }

    // Convert List<NameValuePair> to JSON string
    public static String toJson(List<NameValuePair> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(list);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert List<NameValuePair> to JSON", e);
        }
    }

    // Specific helpers for headers, query, path
    public static List<NameValuePair> parseHeaders(String json) {
        return parseNameValueList(json);
    }

    public static List<NameValuePair> parseQueryParams(String json) {
        return parseNameValueList(json);
    }

    public static List<NameValuePair> parsePathParams(String json) {
        return parseNameValueList(json);
    }

    public static String toJsonHeaders(List<NameValuePair> list) {
        return toJson(list);
    }

    public static String toJsonQueryParams(List<NameValuePair> list) {
        return toJson(list);
    }

    public static String toJsonPathParams(List<NameValuePair> list) {
        return toJson(list);
    }
}
