package com.dm.ding;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.dm.dingding.model.DingClientConfig;
import com.dm.dingding.service.DingService;
import com.dm.dingding.service.impl.DefaultDingServiceImpl;

@Configuration
public class ApiConfig {

	@Bean
	public DingService dingService() {
		return new DefaultDingServiceImpl(clientConfig());
	}

	@Bean
	public DingClientConfig clientConfig() {
		DingClientConfig config = new DingClientConfig("dingtkomgnkfu5vwdayr",
				"zp6pOwW5keIDS_Gkc_9n-9fKzlxmmGYOyFy8kH4XGyzAcNFkiczPfR3-QgeSMvop");
		return config;
	}

//	@Bean
//	public RestTemplate restTemplate() {
//		RestTemplate template = new RestTemplate();
//		template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
//		return template;
//	}

}
