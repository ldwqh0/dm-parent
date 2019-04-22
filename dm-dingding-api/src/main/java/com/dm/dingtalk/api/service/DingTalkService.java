package com.dm.dingtalk.api.service;

import java.util.List;

import com.dm.dingtalk.api.model.UserInfo;
import com.dm.dingtalk.api.request.OapiUserCreateRequest;
import com.dm.dingtalk.api.request.OapiUserUpdateRequest;
import com.dm.dingtalk.api.response.OapiDepartmentListResponse.Department;
import com.dm.dingtalk.api.response.OapiRoleListResponse.OpenRoleGroup;
import com.dm.dingtalk.api.response.OapiUserCreateResponse;
import com.dm.dingtalk.api.response.OapiUserGetuserinfoResponse;
import com.dm.dingtalk.api.response.OapiUserUpdateResponse;

public interface DingTalkService {
	/**
	 * 获取accessToken
	 * 
	 * @return
	 */
	public String getAccessToken();

	/**
	 * 根据用户ID获取用户信息
	 * 
	 * @param userid
	 * @return
	 */
	public UserInfo getUserInfoByUserid(String userid);

	/**
	 * 获取部门列表信息
	 * 
	 * @return
	 */
	public List<Department> fetchDepartments();

	public List<OpenRoleGroup> fetchRoleGroups();

	public OapiUserCreateResponse createUser(OapiUserCreateRequest request);

	public OapiUserUpdateResponse updateUser(OapiUserUpdateRequest request);

	/**
	 * 根据免登录授权码获取用户信息
	 * 
	 * @param authCode
	 * @return
	 */
	public OapiUserGetuserinfoResponse getUserByAuthCode(String authCode);
}
