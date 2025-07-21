package com.bpm.common.utils;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TokenUtil {
	private static final Logger log = LoggerFactory.getLogger(TokenUtil.class);
	
	public static int parseTokenDuration(String durationStr, int defaultValue) {		
        try {
            return Optional.ofNullable(durationStr)
                           .map(Integer::parseInt)
                           .filter(d -> d > 0) // Ensure positive value
                           .orElse(defaultValue);
        } catch (NumberFormatException e) {
            log.warn("Invalid TOKEN_DURATION: " + durationStr + ", using default: " + defaultValue);
            return defaultValue;
        }
    }
}
