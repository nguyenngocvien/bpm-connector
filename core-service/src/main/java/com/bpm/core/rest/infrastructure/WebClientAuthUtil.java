package com.bpm.core.rest.infrastructure;

import org.springframework.web.reactive.function.client.WebClient;

import com.bpm.core.auth.domain.AuthConfig;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class WebClientAuthUtil {

	public static WebClient.Builder applyAuth(WebClient.Builder builder, AuthConfig config) {
	    Map<String, String> headers = generateHeaders(config);
	    return builder.defaultHeaders(h -> headers.forEach(h::set));
	}

	public static WebClient.RequestHeadersSpec<?> applyToRequest(WebClient.RequestHeadersSpec<?> request, AuthConfig config) {
	    Map<String, String> headers = generateHeaders(config);
	    headers.forEach(request::header);
	    return request;
	}

	private static Map<String, String> generateHeaders(AuthConfig config) {
	    Map<String, String> headerMap = new HashMap<>();
	    if (config == null || !config.isActive() || config.getAuthType() == null) return headerMap;

	    switch (config.getAuthType()) {
	        case BASIC:
	            String creds = config.getUsername() + ":" + config.getPassword();
	            String encoded = Base64.getEncoder().encodeToString(creds.getBytes(StandardCharsets.UTF_8));
	            headerMap.put("Authorization", "Basic " + encoded);
	            break;
	        case BEARER:
	        case OAUTH2: // assume token ready
	            headerMap.put("Authorization", "Bearer " + config.getToken());
	            break;
	        case API_KEY:
	            headerMap.put(config.getApiKeyHeader(), config.getApiKeyValue());
	            break;
	        default:
	            break;
	    }
	    return headerMap;
	}

}
