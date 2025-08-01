package com.bpm.core.model.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestQueryParam {
    private Long id;
    private Long restConfigId;
    private String paramName;
    private String paramValue;
}
