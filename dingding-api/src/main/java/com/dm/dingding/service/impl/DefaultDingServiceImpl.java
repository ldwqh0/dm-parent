package com.dm.dingding.service.impl;

import java.util.Objects;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import com.dm.dingding.model.AccessToken;
import com.dm.dingding.model.DingClientConfig;
import com.dm.dingding.service.DingService;

public class DefaultDingServiceImpl implements DingService, InitializingBean {
	private RestTemplate restTemplate;
	private DingClientConfig clientConfig;
	private static final String SERVER = "https://oapi.dingtalk.com";

	public DefaultDingServiceImpl() {
		super();
	}

	public DefaultDingServiceImpl(DingClientConfig clientConfig) {
		super();
		this.clientConfig = clientConfig;
	}

	@Autowired(required = false)
	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Override
	public AccessToken getAccessToken() {
		String url = SERVER + "/gettoken?appkey={appkey}&appsecret={appsecret}";
		clientConfig.getAppsecret();
		return restTemplate.getForObject(url, AccessToken.class, clientConfig);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// 配置一个默认的restTemplate
		if (Objects.isNull(restTemplate)) {
			this.restTemplate = new RestTemplate();
		}
		Assert.notNull(clientConfig, "The clientConfig can not be null");
	}

}
