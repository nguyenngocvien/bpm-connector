package com.bpm.common.utils;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebSphereUtil {
	private static final String JNDI_NAMING_PREFIX = "java:comp/env/";
	private static final Logger log = LoggerFactory.getLogger(WebSphereUtil.class);
	
	public static <T> T getEnvVar(String propertyName, Class<T> type) {
	    try {
	        Context ctx = new InitialContext();
	        Object value = ctx.lookup(JNDI_NAMING_PREFIX + propertyName);

	        if (value == null) {
	            return null;
	        }
	        
	        if (type.isInstance(value)) {
	            return type.cast(value);
	        } 
	        
	        if (type == String.class) {
	            return type.cast(value.toString());
	        }

	        if (type == Integer.class && value instanceof String) {
	            return type.cast(Integer.valueOf((String) value));
	        }

	        if (type == Double.class && value instanceof String) {
	            return type.cast(Double.valueOf((String) value));
	        }

	        if (type == Boolean.class && value instanceof String) {
	            return type.cast(Boolean.valueOf((String) value));
	        }

	        log.info("Type mismatch: Expected " + type.getName() + " but got " + value.getClass().getName());
	        return null;
	    } catch (NamingException | NumberFormatException e) {
	    	log.info(propertyName + " JNDI Lookup failed: " + e.getMessage());
	        return null;
	    }
	}
    
    public static String getPasswordFromAlias(String alias) {
        try {
            InitialContext ctx = new InitialContext();
            return (String) ctx.lookup(JNDI_NAMING_PREFIX + alias);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void main(String[] args) {
        String value = getEnvVar("myCustomProperty", String.class);
        System.out.println("Custom Property Value: " + value);
    }
}
