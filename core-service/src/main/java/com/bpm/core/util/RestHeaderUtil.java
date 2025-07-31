package com.bpm.core.util;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.bpm.core.model.rest.RestHeader;

public class RestHeaderUtil {

    public static Map<String, String> toHeaderMap(List<RestHeader> headerList) {
        if (headerList == null || headerList.isEmpty()) {
            return Collections.emptyMap();  // return immutable empty map
        }
        return headerList.stream()
                .filter(h -> h.getHeaderName() != null && h.getHeaderValue() != null)
                .collect(Collectors.toMap(RestHeader::getHeaderName, RestHeader::getHeaderValue));
    }
}
