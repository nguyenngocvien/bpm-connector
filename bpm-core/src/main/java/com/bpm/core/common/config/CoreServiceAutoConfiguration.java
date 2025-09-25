package com.bpm.core.common.config;

import java.util.List;

import org.graalvm.polyglot.Context;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.support.TransactionTemplate;

import com.bpm.core.auth.cache.AuthServiceCache;
import com.bpm.core.auth.domain.AuthProperties;
import com.bpm.core.auth.provider.BasicAuthProvider;
import com.bpm.core.auth.provider.JwtAuthProvider;
import com.bpm.core.auth.repository.AuthConfigRepository;
import com.bpm.core.auth.service.AuthManager;
import com.bpm.core.auth.service.AuthRepositoryService;
import com.bpm.core.common.util.ServiceLogHelper;
import com.bpm.core.db.infrastructure.DbExecutorHelper;
import com.bpm.core.db.repository.DataSourceRepository;
import com.bpm.core.db.repository.DbServiceConfigRepository;
import com.bpm.core.db.service.DataSourceConfigService;
import com.bpm.core.db.service.DbServiceConfigService;
import com.bpm.core.document.repository.DocumentConfigRepository;
import com.bpm.core.document.repository.DocumentTemplateRepository;
import com.bpm.core.document.service.TemplateRepositoryService;
import com.bpm.core.mail.repository.MailServiceConfigRepository;
import com.bpm.core.mail.service.MailServiceConfigService;
import com.bpm.core.rest.infrastructure.RestClientHelper;
import com.bpm.core.rest.infrastructure.RestRequestMapper;
import com.bpm.core.rest.infrastructure.RestResponseMapper;
import com.bpm.core.rest.repository.RestServiceConfigRepository;
import com.bpm.core.rest.service.RestServiceConfigService;
import com.bpm.core.server.repository.ServerConfigRepository;
import com.bpm.core.server.service.ServerRepositoryService;
import com.bpm.core.serviceconfig.interfaces.ServiceInvoker;
import com.bpm.core.serviceconfig.repository.ServiceConfigRepository;
import com.bpm.core.serviceconfig.service.ServiceConfigRepositoryService;
import com.bpm.core.serviceconfig.service.ServiceDispatcher;
import com.bpm.core.serviceconfig.service.impl.DbExecutor;
import com.bpm.core.serviceconfig.service.impl.EmailSender;
import com.bpm.core.serviceconfig.service.impl.RestInvoker;
import com.bpm.core.servicelog.repository.ServiceLogRepository;
import com.bpm.core.servicelog.service.ServiceLogService;
import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfiguration
@EnableConfigurationProperties(AuthProperties.class)
@EnableJpaRepositories(basePackages = {
    "com.bpm.core.auth.repository",
    "com.bpm.core.db.repository",
    "com.bpm.core.server.repository",
    "com.bpm.core.serviceconfig.repository",
    "com.bpm.core.servicelog.repository",
    "com.bpm.core.rest.repository",
    "com.bpm.core.mail.repository",
    "com.bpm.core.document.repository",
})
@EntityScan(basePackages = {
    "com.bpm.core.auth.domain",
    "com.bpm.core.db.domain",
    "com.bpm.core.server.domain",
    "com.bpm.core.serviceconfig.domain",
    "com.bpm.core.servicelog.domain",
    "com.bpm.core.mail.domain",
    "com.bpm.core.rest.domain",
    "com.bpm.core.document.domain"
})
public class CoreServiceAutoConfiguration {
	
	@Bean
    public BasicAuthProvider basicAuthProvider(AuthProperties properties) {
        return new BasicAuthProvider(properties);
    }

    @Bean
    public JwtAuthProvider jwtAuthProvider(AuthProperties properties) {
        return new JwtAuthProvider(properties);
    }

    @Bean
    public AuthManager authManager(BasicAuthProvider basicAuthProvider,
                                   JwtAuthProvider jwtAuthProvider) {
        return new AuthManager(basicAuthProvider, jwtAuthProvider);
    }
    
    @Bean
    public AuthRepositoryService authService(AuthConfigRepository repository) {
        return new AuthRepositoryService(repository);
    }
    
    @Bean
    public ServerRepositoryService serverRepositoryService (ServerConfigRepository serverRepository) {
        return new ServerRepositoryService(serverRepository);
    }
    
    @Bean
    public DataSourceConfigService dataSourceConfigService (DataSourceRepository dataSourceRepository) {
        return new DataSourceConfigService(dataSourceRepository);
    }
    
    @Bean
    public DbServiceConfigService dbServiceConfigService (DbServiceConfigRepository repository, ObjectMapper objectMapper) {
        return new DbServiceConfigService(repository, objectMapper);
    }
    
    @Bean
    public AuthServiceCache authServiceCache(AuthConfigRepository repository) {
        return new AuthServiceCache(repository);
    }

    @Bean
    public RestServiceConfigService restService(RestServiceConfigRepository repository) {
        return new RestServiceConfigService(repository);
    }
    
    @Bean
    public TemplateRepositoryService templateService(DocumentTemplateRepository repository) {
        return new TemplateRepositoryService(repository);
    }
    
    @Bean
    public MailServiceConfigService mailServiceConfigService(MailServiceConfigRepository repository) {
        return new MailServiceConfigService(repository);
    }

    @Bean
    public ServiceLogService serviceLogService(ServiceLogRepository repository) {
        return new ServiceLogService(repository);
    }
    
    @Bean
    public ServiceLogHelper logHelper(ServiceLogService logService) {
        return new ServiceLogHelper(logService);
    }

    @Bean
    public ServiceConfigRepositoryService serviceConfigService(
    		ServiceConfigRepository serviceConfigRepo,
            DataSourceConfigService dataSourceService,
            DbServiceConfigService dbService,
            RestServiceConfigService restService,
            MailServiceConfigService mailService,
            DocumentConfigRepository docRepository) {

        return new ServiceConfigRepositoryService(
        		serviceConfigRepo,
                dbService,
                restService,
                mailService,
                docRepository
        );
    }
    
    @Bean
    public Context graalVmContext() {
        return Context.newBuilder("js")
                .allowHostClassLookup(s -> true)
                .build();
    }

    @Bean
    public RestRequestMapper requestMapper(Context graalVmContext) {
    	return new RestRequestMapper(graalVmContext);
    }
    
    @Bean
    public RestResponseMapper responseMapper(Context graalVmContext) {
    	return new RestResponseMapper(graalVmContext);
    }
    
    @Bean
    public DbExecutorHelper dbExecutorHelper(
    		DbServiceConfigService dbService,
    		DataSourceConfigService dataSourceService) {
    	return new DbExecutorHelper(dbService, dataSourceService);
    }
    
    @Bean
    public DbExecutor dbExecutor(
    		DbExecutorHelper dbHelpe,
    		ServiceLogHelper logHelper,
    		TransactionTemplate txTemplate) {
    	return new DbExecutor(dbHelpe, logHelper, txTemplate);
    }
    
    @Bean
    public RestClientHelper clientHelper(
    		ServerRepositoryService serverService,
    		RestServiceConfigService restService,
    		AuthServiceCache authCache,
    		RestRequestMapper requestMapper,
    		RestResponseMapper responseMapper) {
    	
    	return new RestClientHelper(serverService, restService, authCache, requestMapper, responseMapper);
    }
    
    @Bean
    public RestInvoker restInvoker(
    		RestClientHelper clientHelper,
    		ServiceLogHelper logHelper) {
    	return new RestInvoker(clientHelper, logHelper);
    }
    
    @Bean
    public EmailSender emailSender(MailServiceConfigService mailService, ServerRepositoryService serverService, AuthRepositoryService authService) {
    	return new EmailSender(mailService, serverService, authService);
    }
    
    @Bean
    public ServiceDispatcher dispatcher(ServiceConfigRepositoryService configService, List<ServiceInvoker> invokers) {
        return new ServiceDispatcher(configService, invokers);
    }
}
