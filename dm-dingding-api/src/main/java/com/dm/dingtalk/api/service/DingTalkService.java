package com.dm.dingtalk.api.service;

import java.util.Collection;
import java.util.List;

import com.dm.dingtalk.api.response.OapiRoleAddrolesforempsResponse;
import com.dm.dingtalk.api.request.OapiUserCreateRequest;
import com.dm.dingtalk.api.request.OapiUserUpdateRequest;
import com.dm.dingtalk.api.request.OapiWorkrecordAddRequest;
import com.dm.dingtalk.api.request.OapiWorkrecordGetbyuseridRequest;
import com.dm.dingtalk.api.request.OapiWorkrecordUpdateRequest;
import com.dm.dingtalk.api.response.OapiDepartmentListResponse.Department;
import com.dm.dingtalk.api.response.OapiRoleListResponse.OpenRoleGroup;
import com.dm.dingtalk.api.response.OapiUserCreateResponse;
import com.dm.dingtalk.api.response.OapiUserGetDeptMemberResponse;
import com.dm.dingtalk.api.response.OapiUserGetResponse;
import com.dm.dingtalk.api.response.OapiUserGetuserinfoResponse;
import com.dm.dingtalk.api.response.OapiUserUpdateResponse;
import com.dm.dingtalk.api.response.OapiWorkrecordAddResponse;
import com.dm.dingtalk.api.response.OapiWorkrecordGetbyuseridResponse;
import com.dm.dingtalk.api.response.OapiWorkrecordUpdateResponse;

/**
 * 钉钉服务器交互API
 * 
 * @author LiDong
 *
 */
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
//	public UserInfo getUserInfoByUserid(String userid);

    /**
     * 获取部门列表信息
     * 
     * @return
     */
    public List<Department> fetchDepartments();

    /**
     * 获取角色组
     * 
     * @return
     */
    public List<OpenRoleGroup> fetchRoleGroups();

    /**
     * 创建一个钉钉用户
     * 
     * @param request
     * @return
     */
    public OapiUserCreateResponse createUser(OapiUserCreateRequest request);

    /**
     * 更新一个钉钉用户
     * 
     * @param request
     * @return
     */
    public OapiUserUpdateResponse updateUser(OapiUserUpdateRequest request);

    /**
     * 根据免登录授权码获取用户信息
     * 
     * @param authCode
     * @return
     */
    public OapiUserGetuserinfoResponse getUserByAuthCode(String authCode);

    /**
     * 获取一个部门的用户列表
     * 
     * @param depId
     * @return
     */
    public OapiUserGetDeptMemberResponse fetchUsers(Long depId);

    /**
     * 获取一个用户的信息
     * 
     * @param userid
     * @return
     */
    public OapiUserGetResponse fetchUserById(String userid);

    /**
     * 从钉钉服务器上删除一个用户
     * 
     * @param userid
     */
    public void deleteUser(String userid);

    /**
     * 批量设置用户角色信息
     * 
     * @param userIds
     * @param roleIds
     * @return
     */
    public OapiRoleAddrolesforempsResponse batchSetUserRole(Collection<String> userIds, Collection<Long> roleIds);

    /**
     * 添加待办事项
     * 
     * @param request
     * @return
     */
    public OapiWorkrecordAddResponse addWorkRecord(OapiWorkrecordAddRequest request);

    /**
     * 更新待办事项，该操作会将待办事项从用户的待办事项列表中删除
     * 
     * @return
     */
    public OapiWorkrecordUpdateResponse updateWorkRecord(OapiWorkrecordUpdateRequest request);

    /**
     * 获取某个用户的待办事项列表，每次最多50条
     * 
     * @param request
     * @return
     */
    public OapiWorkrecordGetbyuseridResponse getWorkRecordByUserid(OapiWorkrecordGetbyuseridRequest request);
}
