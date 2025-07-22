package com.bpm.auth;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bpm.core.constant.ENV;
import com.bpm.core.util.WebSphereUtil;

public class BasicAuth {
	private static final Logger log = LoggerFactory.getLogger(BasicAuth.class);
	
	public static String createBasicAuth() {
        String username = WebSphereUtil.getEnvVar(ENV.AUTH_USER, String.class);
        String password = WebSphereUtil.getPasswordFromAlias(ENV.AUTH_J2C_ALIAS);

        // Encode only if both username and password are non-null and non-empty
        if (username != null && password != null && !username.isEmpty() && !password.isEmpty()) {
            String auth = username + ":" + password;
            return Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        }

        log.info("No credentials found.");
        return null; // Return null if credentials are missing
    }
	
	public static String createBasicAuth(String username, String password) {

        // Encode only if both username and password are non-null and non-empty
        if (username != null && password != null && !username.isEmpty() && !password.isEmpty()) {
            String auth = username + ":" + password;
            return Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        }

        log.info("No credentials found.");
        return null; // Return null if credentials are missing
    }
}
