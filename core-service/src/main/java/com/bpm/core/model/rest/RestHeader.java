package com.bpm.core.model.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestHeader {
    private Long id;
    private Long restConfigId;
    private String headerName;
    private String headerValue;
}
