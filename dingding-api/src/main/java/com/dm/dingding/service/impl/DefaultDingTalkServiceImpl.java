package com.dm.dingding.service.impl;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import com.dm.dingding.model.DingClientConfig;
import com.dm.dingding.model.UserInfo;
import com.dm.dingding.model.response.AccessTokenResponse;
import com.dm.dingding.model.response.OapiDepartmentListResponse;
import com.dm.dingding.model.response.OapiDepartmentListResponse.Department;
import com.dm.dingding.service.DingTalkService;

public class DefaultDingTalkServiceImpl implements DingTalkService, InitializingBean {
	private RestTemplate restTemplate;
	private DingClientConfig clientConfig;
	private static final String SERVER = "https://oapi.dingtalk.com";
	private final Object tokenLock = new Object();

	/**
	 * 已经存在的token,不要再代码中直接使用该token <br>
	 * 请使用 getAccessToken 方法获取可用的token
	 * 
	 */
	private AccessTokenResponse existToken;

	public DefaultDingTalkServiceImpl() {
		super();
	}

	public DefaultDingTalkServiceImpl(DingClientConfig clientConfig) {
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
	public AccessTokenResponse getAccessToken() {
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
	private AccessTokenResponse getAccessTokenFromServer() {
		String url = SERVER + "/gettoken?appkey={appkey}&appsecret={appsecret}";
		clientConfig.getAppsecret();
		return restTemplate.getForObject(url, AccessTokenResponse.class, clientConfig);
	}

	private boolean validateAccessToken(AccessTokenResponse token) {
		if (Objects.isNull(token)) {
			return false;
		}
		ZonedDateTime now = ZonedDateTime.now();
		if (now.isAfter(token.getExpireDate())) {
			return false;
		}
		return true;
	}

	@Override
	public List<Department> fetchDepartments() {
		String url = SERVER + "/department/list?access_token={0}";
		OapiDepartmentListResponse response = restTemplate.getForObject(url, OapiDepartmentListResponse.class,
				getAccessToken().getAccessToken());
		return response.getDepartment();
	}
}
