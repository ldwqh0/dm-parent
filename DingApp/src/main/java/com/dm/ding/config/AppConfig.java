package com.dm.ding.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dm.dingding.model.DingClientConfig;
import com.dm.dingding.service.DingService;
import com.dm.dingding.service.impl.DefaultDingServiceImpl;

@Configuration
public class AppConfig {

	@Bean
	@ConfigurationProperties(prefix = "dingding")
	public DingClientConfig clientConfig() {
		return new DingClientConfig();
	}

	@Bean
	public DingService defaultDingService() {
		return new DefaultDingServiceImpl(clientConfig());
	}

}
