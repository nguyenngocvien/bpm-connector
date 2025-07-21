package com.bpm.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    @Configuration
    @Profile({"local"})
    static class JdbcConfig {
    	
    	@Bean
    	@ConfigurationProperties(prefix = "spring.datasource")
        public DataSource dataSource() {
            return DataSourceBuilder.create()
            		.type(DriverManagerDataSource.class)
            		.build();
        }

        @Bean
        public JdbcTemplate jdbcTemplate(DataSource dataSource) {
            return new JdbcTemplate(dataSource);
        }
    }

    @Configuration
    @Profile({"dev", "prod", "uat"})
    static class JndiConfig {

    	@Value("${spring.datasource.jndi-name}")
        private String jndiName;
    	
        @Bean
        public DataSource dataSource() throws NamingException {
        	if (jndiName == null) {
                throw new IllegalArgumentException("JNDI name is null. Check spring.datasource.jndi-name configuration.");
            }
        	
            Context ctx = new InitialContext();
            return (DataSource) ctx.lookup(jndiName);
        }

        @Bean
        public JdbcTemplate jdbcTemplate(DataSource dataSource) {
            return new JdbcTemplate(dataSource);
        }
    }
}

