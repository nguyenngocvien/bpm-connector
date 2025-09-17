package com.bpm.core.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import com.bpm.core.datasource.repository.DataSourceRepository;
import com.bpm.core.datasource.repository.DbServiceRepository;
import com.bpm.core.datasource.service.DataSourceService;
import com.bpm.core.document.repository.FileServiceRepository;
import com.bpm.core.mail.repository.MailServiceRepository;
import com.bpm.core.mail.service.EmailSender;
import com.bpm.core.rest.repository.RestServiceRepository;
import com.bpm.core.server.repository.ServerRepository;
import com.bpm.core.serviceconfig.repository.ServiceConfigRepository;
import com.bpm.core.serviceconfig.service.ServiceConfigService;

@Configuration
public class ServiceAutoConfiguration {
	
	@Bean
	public ServerRepository serverRepository(JdbcTemplate jdbcTemplate) {
		return new ServerRepository(jdbcTemplate);
	}
	
	@Bean
	public ServiceConfigRepository serviceConfigRepository(JdbcTemplate jdbcTemplate) {
		return new ServiceConfigRepository(jdbcTemplate);
	}
	
	@Bean
	public DataSourceRepository dataSourceRepository(JdbcTemplate jdbcTemplate) {
		return new DataSourceRepository(jdbcTemplate);
	}
	
	@Bean
	public DataSourceService dataSourceService(DataSourceRepository repository) {
		return new DataSourceService(repository);
	}
	
	@Bean
	public DbServiceRepository dbServiceRepository(JdbcTemplate jdbcTemplate) {
		return new DbServiceRepository(jdbcTemplate);
	}
	
	@Bean
	public RestServiceRepository restServiceRepository(JdbcTemplate jdbcTemplate) {
		return new RestServiceRepository(jdbcTemplate);
	}
	
	@Bean
	public MailServiceRepository mailServiceRepository(JdbcTemplate jdbcTemplate) {
		return new MailServiceRepository(jdbcTemplate);
	}
	
	@Bean
	public FileServiceRepository fileServiceRepository(JdbcTemplate jdbcTemplate) {
		return new FileServiceRepository(jdbcTemplate);
	}
	
	@Bean
	public ServiceConfigService configService(ServiceConfigRepository serviceConfigRepository,
			DataSourceRepository dataSourceRepository,
			DbServiceRepository dbServiceRepository,
			RestServiceRepository restServiceRepository,
			MailServiceRepository mailServiceRepository,
			FileServiceRepository fileServiceRepository) {
		return new ServiceConfigService(serviceConfigRepository, dataSourceRepository, dbServiceRepository, restServiceRepository, mailServiceRepository, fileServiceRepository);
	}
	
	@Bean
	public EmailSender emailSender() {
		return new EmailSender();
	}

}
