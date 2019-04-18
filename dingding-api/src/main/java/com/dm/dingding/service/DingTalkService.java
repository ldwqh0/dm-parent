package com.dm.dingding.service;

import java.util.List;

import com.dm.dingding.model.UserInfo;
import com.dm.dingding.model.response.AccessTokenResponse;
import com.dm.dingding.model.response.OapiDepartmentListResponse.Department;

public interface DingTalkService {
	/**
	 * 获取accessToken
	 * 
	 * @return
	 */
	public AccessTokenResponse getAccessToken();

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
}
