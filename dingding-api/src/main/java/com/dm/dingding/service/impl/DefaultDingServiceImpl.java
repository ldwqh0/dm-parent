package com.dm.dingding.service.impl;

import java.time.ZonedDateTime;
import java.util.Objects;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import com.dm.dingding.model.AccessToken;
import com.dm.dingding.model.DingClientConfig;
import com.dm.dingding.model.UserInfo;
import com.dm.dingding.service.DingService;

public class DefaultDingServiceImpl implements DingService, InitializingBean {
	private RestTemplate restTemplate;
	private DingClientConfig clientConfig;
	private static final String SERVER = "https://oapi.dingtalk.com";
	private final Object tokenLock = new Object();

	/**
	 * 已经存在的token,不要再代码中直接使用该token <br>
	 * 请使用 getAccessToken 方法获取可用的token
	 * 
	 */
	private AccessToken existToken;

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

	/**
	 * 获取可用的accessToken
	 * 
	 * @return
	 */
	@Override
	public AccessToken getAccessToken() {
		if (!validateAccessToken(this.existToken)) {
			synchronized (tokenLock) {
				if (!validateAccessToken(existToken)) {
					existToken = getAccessTokenFromServer();
				}
			}
		}
		return existToken;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// 配置一个默认的restTemplate
		if (Objects.isNull(restTemplate)) {
			this.restTemplate = new RestTemplate();
		}
		Assert.notNull(clientConfig, "The clientConfig can not be null");
	}

	@Override
	public UserInfo getUserInfoByUserid(String userid) {
		String url = SERVER + "/user/get?access_token={0}&userid={1}";
		return restTemplate.getForObject(url, UserInfo.class, getAccessToken().getAccessToken(), userid);
	}

	/**
	 * 从服务器获取AccessToken
	 * 
	 * @return
	 */
	private AccessToken getAccessTokenFromServer() {
		String url = SERVER + "/gettoken?appkey={appkey}&appsecret={appsecret}";
		clientConfig.getAppsecret();
		return restTemplate.getForObject(url, AccessToken.class, clientConfig);
	}

	private boolean validateAccessToken(AccessToken token) {
		if (Objects.isNull(token)) {
			return false;
		}
		ZonedDateTime now = ZonedDateTime.now();
		if (now.isAfter(token.getExpireDate())) {
			return false;
		}
		return true;
	}
}