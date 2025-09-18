package com.bpm.core.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import com.bpm.core.auth.cache.AuthServiceCache;
import com.bpm.core.db.service.DataSourceService;
import com.bpm.core.db.service.DbService;
import com.bpm.core.document.service.FileService;
import com.bpm.core.mail.service.EmailSender;
import com.bpm.core.mail.service.MailService;
import com.bpm.core.rest.service.RestService;
import com.bpm.core.server.service.ServerService;
import com.bpm.core.serviceconfig.service.ServiceConfigService;
import com.bpm.core.serviceconfig.service.ServiceDispatcher;
import com.bpm.core.serviceconfig.service.impl.DBInvoker;
import com.bpm.core.serviceconfig.service.impl.RESTInvoker;
import com.bpm.core.servicelog.service.ServiceLogService;

@Configuration
public class CoreServiceAutoConfiguration {

    @Bean
    public ServerService serverService(JdbcTemplate jdbcTemplate) {
        return new ServerService(new com.bpm.core.server.repository.ServerRepository(jdbcTemplate));
    }

    @Bean
    public DataSourceService dataSourceService(JdbcTemplate jdbcTemplate) {
        return new DataSourceService(new com.bpm.core.db.repository.DataSourceRepository(jdbcTemplate));
    }

    @Bean
    public DbService dbService(JdbcTemplate jdbcTemplate) {
        return new DbService(new com.bpm.core.db.repository.DbServiceRepository(jdbcTemplate));
    }

    @Bean
    public RestService restService(JdbcTemplate jdbcTemplate) {
        return new RestService(new com.bpm.core.rest.repository.RestRepository(jdbcTemplate));
    }

    @Bean
    public MailService mailService(JdbcTemplate jdbcTemplate) {
        return new MailService(new com.bpm.core.mail.repository.MailRepository(jdbcTemplate));
    }

    @Bean
    public FileService fileService(JdbcTemplate jdbcTemplate) {
        return new FileService(new com.bpm.core.document.repository.FileServiceRepository(jdbcTemplate));
    }
    
    @Bean
    public ServiceLogService serviceLogService(JdbcTemplate jdbcTemplate) {
        return new ServiceLogService(new com.bpm.core.servicelog.repository.ServiceLogRepository(jdbcTemplate));
    }

    @Bean
    public ServiceConfigService serviceConfigService(
            JdbcTemplate jdbcTemplate,
            DataSourceService dataSourceService,
            DbService dbService,
            RestService restService,
            MailService mailService,
            FileService fileService) {

        return new ServiceConfigService(
                new com.bpm.core.serviceconfig.repository.ServiceConfigRepository(jdbcTemplate),
                dbService,
                restService,
                mailService,
                fileService
        );
    }
    
    @Bean
    public ServiceDispatcher dispatcher(
    		PlatformTransactionManager txManager,
    		DbService dbService,
    		DataSourceService dataSourceService,
    		ServerService serverService,
    		RestService restService,
    		AuthServiceCache authServiceCache,
    		ServiceLogService serviceLogService,
            ServiceConfigService service) {
    	
    	
        return new ServiceDispatcher(
        		new DBInvoker(dbService, dataSourceService, serviceLogService, txManager),
        		new RESTInvoker(serverService, restService, serviceLogService, authServiceCache),
        		service);
    }

    @Bean
    public EmailSender emailSender() {
        return new EmailSender();
    }
}
