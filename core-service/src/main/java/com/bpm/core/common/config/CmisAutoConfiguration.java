package com.bpm.core.common.config;

import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bpm.core.document.domain.CmisProperties;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(CmisProperties.class)
public class CmisAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public Session cmisSession(CmisProperties properties) {
        Map<String, String> params = new HashMap<>();
        params.put(SessionParameter.USER, properties.getUsername());
        params.put(SessionParameter.PASSWORD, properties.getPassword());
        params.put(SessionParameter.ATOMPUB_URL, properties.getAtompubUrl());
        params.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
        params.put(SessionParameter.REPOSITORY_ID, properties.getRepositoryId());

        params.put(SessionParameter.CONNECT_TIMEOUT, String.valueOf(properties.getConnectTimeoutMs()));
        params.put(SessionParameter.READ_TIMEOUT, String.valueOf(properties.getReadTimeoutMs()));

        return SessionFactoryImpl.newInstance().createSession(params);
    }
}
