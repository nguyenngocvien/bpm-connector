package com.bpm.core.common.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import com.bpm.core.auth.cache.AuthServiceCache;
import com.bpm.core.auth.domain.AuthProperties;
import com.bpm.core.auth.provider.BasicAuthProvider;
import com.bpm.core.auth.provider.JwtAuthProvider;
import com.bpm.core.auth.repository.AuthConfigRepository;
import com.bpm.core.auth.service.AuthManager;
import com.bpm.core.auth.service.AuthRepositoryService;
import com.bpm.core.db.service.DataSourceConfigService;
import com.bpm.core.db.service.DbServiceConfigService;
import com.bpm.core.document.repository.DocumentConfigRepository;
import com.bpm.core.mail.service.EmailSender;
import com.bpm.core.mail.service.MailServiceConfigService;
import com.bpm.core.rest.repository.RestServiceConfigRepository;
import com.bpm.core.rest.service.RestServiceConfigService;
import com.bpm.core.server.service.ServerService;
import com.bpm.core.serviceconfig.service.ServiceConfigRepositoryService;
import com.bpm.core.serviceconfig.service.ServiceDispatcher;
import com.bpm.core.serviceconfig.service.impl.DbInvoker;
import com.bpm.core.serviceconfig.service.impl.RestInvoker;
import com.bpm.core.servicelog.service.ServiceLogService;

@Configuration
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
    public AuthServiceCache authServiceCache(AuthConfigRepository repository) {
        return new AuthServiceCache(repository);
    }

    @Bean
    public ServerService serverService(JdbcTemplate jdbcTemplate) {
        return new ServerService(new com.bpm.core.server.repository.ServerRepository(jdbcTemplate));
    }

    @Bean
    public RestServiceConfigService restService(JdbcTemplate jdbcTemplate) {
        return new RestServiceConfigService(new com.bpm.core.rest.repository.RestServiceConfigRepository(jdbcTemplate));
    }

    @Bean
    public ServiceLogService serviceLogService(JdbcTemplate jdbcTemplate) {
        return new ServiceLogService(new com.bpm.core.servicelog.repository.ServiceLogRepository(jdbcTemplate));
    }

    @Bean
    public ServiceConfigRepositoryService serviceConfigService(
            JdbcTemplate jdbcTemplate,
            DataSourceConfigService dataSourceService,
            DbServiceConfigService dbService,
            RestServiceConfigService restService,
            MailServiceConfigService mailService,
            DocumentConfigRepository docRepository) {

        return new ServiceConfigRepositoryService(
                new com.bpm.core.serviceconfig.repository.ServiceConfigRepository(jdbcTemplate),
                dbService,
                new RestServiceConfigService(new RestServiceConfigRepository(jdbcTemplate)),
                mailService,
                docRepository
        );
    }
    
    @Bean
    public ServiceDispatcher dispatcher(
    		PlatformTransactionManager txManager,
    		DbServiceConfigService dbService,
    		DataSourceConfigService dataSourceService,
    		ServerService serverService,
    		RestServiceConfigService restService,
    		AuthServiceCache authServiceCache,
    		ServiceLogService serviceLogService,
            ServiceConfigRepositoryService service) {
    	
    	
        return new ServiceDispatcher(
        		service,
        		new DbInvoker(dbService, dataSourceService, serviceLogService, txManager),
        		new RestInvoker(serverService, restService, serviceLogService, authServiceCache));
    }

    @Bean
    public EmailSender emailSender() {
        return new EmailSender();
    }
}
