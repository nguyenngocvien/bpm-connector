package com.bpm.api.client;

import com.bpm.api.model.FincoreResponse;

public interface FincoreClient {
    FincoreResponse getAccountInfo(String accountId);
}

