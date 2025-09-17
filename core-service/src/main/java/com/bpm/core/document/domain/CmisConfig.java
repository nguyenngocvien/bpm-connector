package com.bpm.core.document.domain;

import java.util.HashMap;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CmisConfig {

    @Value("${app.cmis.atompubUrl}")
    private String atompubUrl;

    @Value("${app.cmis.repositoryId}")
    private String repositoryId;

    @Value("${app.cmis.username}")
    private String username;

    @Value("${app.cmis.password}")
    private String password;

    @Value("${app.cmis.connectTimeoutMs:10000}")
    private int connectTimeoutMs;

    @Value("${app.cmis.readTimeoutMs:30000}")
    private int readTimeoutMs;

    @Bean
    public Session cmisSession() {
        Map<String, String> params = new HashMap<>();
        params.put(SessionParameter.USER, username);
        params.put(SessionParameter.PASSWORD, password);
        params.put(SessionParameter.ATOMPUB_URL, atompubUrl);
        params.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
        params.put(SessionParameter.REPOSITORY_ID, repositoryId);

        // Optional HTTP timeouts (OpenCMIS uses Apache HttpClient under the hood)
        params.put(SessionParameter.CONNECT_TIMEOUT, String.valueOf(connectTimeoutMs));
        params.put(SessionParameter.READ_TIMEOUT, String.valueOf(readTimeoutMs));

        return SessionFactoryImpl.newInstance().createSession(params);
    }
}