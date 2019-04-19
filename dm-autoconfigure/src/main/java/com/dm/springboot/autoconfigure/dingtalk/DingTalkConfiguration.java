package com.dm.springboot.autoconfigure.dingtalk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dm.dingding.model.DingClientConfig;
import com.dm.dingding.service.DingTalkService;
import com.dm.dingding.service.impl.DefaultDingTalkServiceImpl;

import lombok.extern.slf4j.Slf4j;

@ConditionalOnClass(DefaultDingTalkServiceImpl.class)
@Configuration
@Slf4j
public class DingTalkConfiguration {

	@Bean
	@ConditionalOnMissingBean(DingTalkService.class)
	@ConditionalOnBean(DingClientConfig.class)
	public DingTalkService dingTalkService(@Autowired DingClientConfig config) {
		log.info("Init default dingclient with appkey[" + config.getAppkey() + "]");
		return new DefaultDingTalkServiceImpl(config);
	}

	@ConfigurationProperties(prefix = "dingtalk")
	@Configuration
	@ConditionalOnProperty(prefix = "dingtalk", name = { "appkey", "appsecret" })
	@EnableConfigurationProperties(DingClientConfigCongiguration.class)
	public static class DingClientConfigCongiguration {
		private String appkey;
		private String appsecret;

		public String getAppkey() {
			return appkey;
		}

		public void setAppkey(String appkey) {
			this.appkey = appkey;
		}

		public String getAppsecret() {
			return appsecret;
		}

		public void setAppsecret(String appsecret) {
			this.appsecret = appsecret;
		}

		@Bean
		public DingClientConfig clientConfig() {
			return new DingClientConfig(appkey, appsecret);
		}
	}

}
