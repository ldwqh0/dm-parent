package com.dm.dingtalk.api.service.impl;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import com.dm.dingtalk.api.response.OapiRoleAddrolesforempsResponse;
import com.dm.dingtalk.api.model.DingClientConfig;
import com.dm.dingtalk.api.request.OapiRoleAddrolesforempsRequest;
import com.dm.dingtalk.api.request.OapiUserCreateRequest;
import com.dm.dingtalk.api.request.OapiUserUpdateRequest;
import com.dm.dingtalk.api.response.AccessTokenResponse;
import com.dm.dingtalk.api.response.OapiDepartmentListResponse;
import com.dm.dingtalk.api.response.OapiRoleListResponse;
import com.dm.dingtalk.api.response.TaobaoResponse;
import com.dm.dingtalk.api.response.OapiDepartmentListResponse.Department;
import com.dm.dingtalk.api.response.OapiRoleListResponse.OpenRoleGroup;
import com.dm.dingtalk.api.response.OapiUserCreateResponse;
import com.dm.dingtalk.api.response.OapiUserGetDeptMemberResponse;
import com.dm.dingtalk.api.response.OapiUserGetResponse;
import com.dm.dingtalk.api.response.OapiUserGetuserinfoResponse;
import com.dm.dingtalk.api.response.OapiUserUpdateResponse;
import com.dm.dingtalk.api.service.DingTalkService;

public class DefaultDingTalkServiceImpl implements DingTalkService, InitializingBean {
	private RestTemplate restTemplate;
	private DingClientConfig clientConfig;
	private static final String SERVER = "https://oapi.dingtalk.com";
	private final Object tokenLock = new Object();
	private static final Long USER_NOT_FOUND_CODE = 60121L;
	/**
	 * 已经存在的token,不要再代码中直接使用该token <br>
	 * 请使用 getAccessToken 方法获取可用的token
	 * 
	 */
	private volatile AccessTokenResponse existToken;

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
	public String getAccessToken() {
		if (!validateAccessToken(this.existToken)) {
			synchronized (tokenLock) {
				if (!validateAccessToken(existToken)) {
					existToken = getAccessTokenFromServer();
				}
			}
		}
		return existToken.getAccessToken();
	}

//	@Override
//	public UserInfo getUserInfoByUserid(String userid) {
//		String url = SERVER + "/user/get?access_token={0}&userid={1}";
//		return restTemplate.getForObject(url, UserInfo.class, getAccessToken(), userid);
//	}

	/**
	 * 从服务器获取AccessToken
	 * 
	 * @return
	 */
	private AccessTokenResponse getAccessTokenFromServer() {
		String url = SERVER + "/gettoken?appkey={appkey}&appsecret={appsecret}";
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
				getAccessToken());
		checkResponse(response);
		if (Objects.isNull(response) || CollectionUtils.isEmpty(response.getDepartment())) {
			return Collections.emptyList();
		} else {
			return response.getDepartment();
		}
	}

	@Override
	public List<OpenRoleGroup> fetchRoleGroups() {
		String url = SERVER + "/topapi/role/list?access_token={0}";
		OapiRoleListResponse response = restTemplate.getForObject(url, OapiRoleListResponse.class,
				getAccessToken());
		checkResponse(response);
		if (!Objects.isNull(response) && !Objects.isNull(response.getResult())
				&& CollectionUtils.isNotEmpty(response.getResult().getList())) {
			return response.getResult().getList();
		} else {
			return Collections.emptyList();
		}

	}

	@Override
	public OapiUserCreateResponse createUser(OapiUserCreateRequest request) {
		String url = SERVER + "/user/create?access_token={0}";
		OapiUserCreateResponse response = restTemplate.postForObject(url, request, OapiUserCreateResponse.class,
				getAccessToken());
		// 创建用户，如果用户已经存在于钉钉系统中了,不会做任何修改，但会返回返回已经存在的用户的userid
		if (!Objects.isNull(response) && StringUtils.isNotEmpty(response.getUserid())) {
		} else {
			checkResponse(response);
		}
		return response;
	}

	@Override
	public OapiUserUpdateResponse updateUser(OapiUserUpdateRequest request) {
		String url = SERVER + "/user/update?access_token={0}";
		OapiUserUpdateResponse response = restTemplate.postForObject(url, request, OapiUserUpdateResponse.class,
				getAccessToken());
		checkResponse(response);
		return response;
	}

	@Override
	public OapiUserGetuserinfoResponse getUserByAuthCode(String authCode) {
		String url = SERVER + "/user/getuserinfo?access_token={0}&code={1}";
		OapiUserGetuserinfoResponse response = restTemplate.getForObject(url, OapiUserGetuserinfoResponse.class,
				getAccessToken(), authCode);
		checkResponse(response);
		return response;
	}

	@Override
	public OapiUserGetDeptMemberResponse fetchUsers(Long depId) {
		String url = SERVER + "/user/getDeptMember?access_token={0}&deptId={1}";
		OapiUserGetDeptMemberResponse response = restTemplate.getForObject(url, OapiUserGetDeptMemberResponse.class,
				getAccessToken(), depId);
		checkResponse(response);
		return response;
	}

	@Override
	public OapiUserGetResponse fetchUserById(String userid) {
		String url = SERVER + "/user/get?access_token={0}&userid={1}";
		OapiUserGetResponse response = restTemplate.getForObject(url, OapiUserGetResponse.class, getAccessToken(),
				userid);
		// checkResponse(response);
		if (Objects.isNull(response)) {
			throw new RuntimeException("the response is null");
		} else if (Objects.equals(USER_NOT_FOUND_CODE, response.getErrcode())) {
			return response;
		} else {
			return response;
		}
	}

	@Override
	public OapiRoleAddrolesforempsResponse batchSetUserRole(Collection<String> userIds, Collection<Long> roleIds) {
		String url = SERVER + "/topapi/role/addrolesforemps?access_token={0}";
		OapiRoleAddrolesforempsRequest request = new OapiRoleAddrolesforempsRequest(userIds, roleIds);
		OapiRoleAddrolesforempsResponse response = restTemplate.postForObject(url, request,
				OapiRoleAddrolesforempsResponse.class, getAccessToken());
		checkResponse(response);
		return response;
	}

	/**
	 * 校验响应是否正确
	 * 
	 * 
	 * @param response
	 */
	private void checkResponse(TaobaoResponse response) {
		if (Objects.isNull(response)) {
			throw new RuntimeException("the response is null");
		} else if (!response.isSuccess()) {
			throw new RuntimeException(response.getErrmsg());
		}

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
