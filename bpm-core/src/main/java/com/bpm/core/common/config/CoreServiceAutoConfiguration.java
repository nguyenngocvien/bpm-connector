package com.bpm.core.common.config;

import org.graalvm.polyglot.Context;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.PlatformTransactionManager;

import com.bpm.core.auth.cache.AuthServiceCache;
import com.bpm.core.auth.domain.AuthProperties;
import com.bpm.core.auth.provider.BasicAuthProvider;
import com.bpm.core.auth.provider.JwtAuthProvider;
import com.bpm.core.auth.repository.AuthConfigRepository;
import com.bpm.core.auth.service.AuthManager;
import com.bpm.core.auth.service.AuthRepositoryService;
import com.bpm.core.db.repository.DataSourceRepository;
import com.bpm.core.db.repository.DbServiceConfigRepository;
import com.bpm.core.db.service.DataSourceConfigService;
import com.bpm.core.db.service.DbServiceConfigService;
import com.bpm.core.document.repository.DocumentConfigRepository;
import com.bpm.core.document.repository.DocumentTemplateRepository;
import com.bpm.core.document.service.TemplateRepositoryService;
import com.bpm.core.mail.repository.MailServiceConfigRepository;
import com.bpm.core.mail.service.MailServiceConfigService;
import com.bpm.core.rest.infrastructure.RestRequestMapper;
import com.bpm.core.rest.infrastructure.RestResponseMapper;
import com.bpm.core.rest.repository.RestServiceConfigRepository;
import com.bpm.core.rest.service.RestServiceConfigService;
import com.bpm.core.server.repository.ServerConfigRepository;
import com.bpm.core.server.service.ServerRepositoryService;
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
    public DbExecutor dbExecutor(
    		DbServiceConfigService dbService,
    		DataSourceConfigService dataSourceService,
    		ServiceLogService logService,
    		PlatformTransactionManager txManager) {
    	return new DbExecutor(dbService, dataSourceService, logService, txManager);
    }
    
    @Bean
    public Context graalVmContext() {
        return Context.newBuilder("js")
                .allowHostAccess(true)
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
    public RestInvoker restInvoker(
    		ServerRepositoryService serverService,
    		RestServiceConfigService restService,
    		ServiceLogService logService,
    		AuthServiceCache authCache,
    		RestRequestMapper requestMapper,
    		RestResponseMapper responseMapper) {
    	return new RestInvoker(serverService, restService, logService, authCache, requestMapper, responseMapper);
    }
    
    @Bean
    public EmailSender emailSender(MailServiceConfigService mailService, ServerRepositoryService serverService, AuthRepositoryService authService) {
    	return new EmailSender(mailService, serverService, authService);
    }
    
    @Bean
    public ServiceDispatcher dispatcher(
    		ServiceConfigRepositoryService configService,
    	    DbExecutor dbExecutor,
    	    RestInvoker restInvoker,
    	    EmailSender emailSender) {
    	
        return new ServiceDispatcher(configService, dbExecutor, restInvoker, emailSender);
    }
}
