package com.bpm.core.common.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

import com.bpm.core.monitor.DashboardService;

@AutoConfiguration
public class WebAutoConfig {
	
	@Bean
	public DashboardService dashboardService() {
		return new DashboardService();
	}
}
