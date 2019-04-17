package com.dm.ding;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.dm.dingding.model.DingClientConfig;
import com.dm.dingding.service.DingTalkService;
import com.dm.dingding.service.impl.DefaultDingTalkServiceImpl;

@Configuration
public class ApiConfig {

	@Bean
	public DingTalkService dingService() {
		return new DefaultDingTalkServiceImpl(clientConfig());
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
