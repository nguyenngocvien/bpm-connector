package com.bpm.core.common.config;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
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
import com.bpm.core.servicelog.service.ServiceLogService;
import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfiguration
@EnableConfigurationProperties(AuthProperties.class)
@EnableJpaRepositories(basePackages = {
    "com.bpm.core.auth.repository",
    "com.bpm.core.db.repository",
    "com.bpm.core.server.repository",
    "com.bpm.core.serviceconfig.repository",
    "com.bpm.core.rest.repository",
    "com.bpm.core.mail.repository",
    "com.bpm.core.document.repository",
})
@EntityScan(basePackages = {
    "com.bpm.core.auth.domain",
    "com.bpm.core.db.domain",
    "com.bpm.core.server.domain",
    "com.bpm.core.serviceconfig.domain",
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
    public MailServiceConfigService mailServiceConfigService(MailServiceConfigRepository repository) {
        return new MailServiceConfigService(repository);
    }

    @Bean
    public ServiceLogService serviceLogService(JdbcTemplate jdbcTemplate) {
        return new ServiceLogService(new com.bpm.core.servicelog.repository.ServiceLogRepository(jdbcTemplate));
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
    public ScriptEngine scriptEngine() {
        ScriptEngineManager manager = new ScriptEngineManager();
        System.out.println("Available ScriptEngines: " + manager.getEngineFactories());
        ScriptEngine engine = manager.getEngineByName("graal.js");
        if (engine == null) {
            engine = manager.getEngineByName("js");
        }
        if (engine == null) {
            throw new IllegalStateException("No Graal.js engine found!");
        }
        return engine;
    }

    
    @Bean
    public RestRequestMapper requestMapper(ScriptEngine engine) {
    	return new RestRequestMapper(engine);
    }
    
    @Bean
    public RestResponseMapper responseMapper(ScriptEngine engine) {
    	return new RestResponseMapper(engine);
    }
    
    @Bean
    public RestInvoker restInvoker(
    		ServerRepositoryService serverService,
    		RestServiceConfigService restService,
    		ServiceLogService logService,
    		AuthServiceCache authCache,
    		RestRequestMapper requestMapper,
    		RestResponseMapper responseMapper) {
    	return new RestInvoker(serverService, restService, logService, authCache);
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
