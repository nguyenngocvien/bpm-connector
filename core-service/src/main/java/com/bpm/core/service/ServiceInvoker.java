package com.bpm.core.service;

import java.util.Map;

public interface ServiceInvoker {
    String invoke(String serviceCode, Map<String, Object> params);
}
