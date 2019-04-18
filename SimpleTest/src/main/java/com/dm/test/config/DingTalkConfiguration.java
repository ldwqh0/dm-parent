package com.dm.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dm.dingding.model.DingClientConfig;
import com.dm.dingding.service.DingTalkService;
import com.dm.dingding.service.impl.DefaultDingTalkServiceImpl;

@Configuration
public class DingTalkConfiguration {

	@Bean
	public DingTalkService dingTalkService() {
		DingClientConfig config = new DingClientConfig();
		config.setAppkey("dingtkomgnkfu5vwdayr");
		config.setAppsecret("zp6pOwW5keIDS_Gkc_9n-9fKzlxmmGYOyFy8kH4XGyzAcNFkiczPfR3-QgeSMvop");
		return new DefaultDingTalkServiceImpl(config);
	}

}
