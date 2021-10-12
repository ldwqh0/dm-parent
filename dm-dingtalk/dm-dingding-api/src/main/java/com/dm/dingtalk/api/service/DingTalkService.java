package com.dm.dingtalk.api.service;

import java.util.Collection;
import java.util.List;

import com.dm.dingtalk.api.response.OapiRoleAddrolesforempsResponse;
import com.dm.dingtalk.api.callback.CallbackProperties;
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
import com.dm.dingtalk.api.response.OpenRole;

/**
 * 钉钉服务器交互API
 *
 * @author LiDong
 */
public interface DingTalkService {

    /**
     * 获取部门列表信息
     *
     * @return
     */
    List<Department> fetchDepartments(String corpId);

    /**
     * 获取角色组
     *
     * @return
     */
    List<OpenRoleGroup> fetchRoleGroups(String corpId);

    /**
     * 创建一个钉钉用户
     *
     * @param request
     * @return
     */
    OapiUserCreateResponse createUser(String corpId, OapiUserCreateRequest request);

    /**
     * 更新一个钉钉用户
     *
     * @param request
     * @return
     */
    OapiUserUpdateResponse updateUser(String corpId, OapiUserUpdateRequest request);

    /**
     * 根据免登录授权码获取用户信息
     *
     * @param authCode
     * @return
     */
    OapiUserGetuserinfoResponse getUserByAuthCode(String corpId, String authCode);

    /**
     * 获取一个部门的用户列表
     *
     * @param depId
     * @return
     */
    OapiUserGetDeptMemberResponse fetchUsers(String corpId, Long depId);

    /**
     * 获取一个用户的信息
     *
     * @param userid
     * @return
     */
    OapiUserGetResponse fetchUserById(String corpId, String userid);

    /**
     * 从钉钉服务器上删除一个用户
     *
     * @param userid
     */
    void deleteUser(String corpId, String userid);

    /**
     * 批量设置用户角色信息
     *
     * @param userIds
     * @param roleIds
     * @return
     */
    OapiRoleAddrolesforempsResponse batchSetUserRole(String corpId, Collection<String> userIds,
                                                     Collection<Long> roleIds);

    /**
     * 添加待办事项<br>
     * 参考<a href=
     * "https://ding-doc.dingtalk.com/doc#/serverapi2/gpmpiq">https://ding-doc.dingtalk.com/doc#/serverapi2/gpmpiq</a>
     *
     * @param request
     * @return
     */
    OapiWorkrecordAddResponse addWorkRecord(String corpId, OapiWorkrecordAddRequest request);

    /**
     * 更新待办事项，该操作会将待办事项从用户的待办事项列表中删除<br>
     * 参考<a href=
     * "https://ding-doc.dingtalk.com/doc#/serverapi2/cxls8y">https://ding-doc.dingtalk.com/doc#/serverapi2/cxls8y</a>
     *
     * @return
     */
    OapiWorkrecordUpdateResponse updateWorkRecord(String corpId, OapiWorkrecordUpdateRequest request);

    /**
     * 获取某个用户的待办事项列表，每次最多50条<br>
     * 参考<a href=
     * "https://ding-doc.dingtalk.com/doc#/serverapi2/neevhm">https://ding-doc.dingtalk.com/doc#/serverapi2/neevhm</a>
     *
     * @param request
     * @return
     */
    OapiWorkrecordGetbyuseridResponse getWorkRecordByUserid(String corpId,
                                                            OapiWorkrecordGetbyuseridRequest request);

    /**
     * 获取指定企业的指定角色信息
     *
     * @param corpid 指定的企业
     * @param roleid 指定的角色
     * @return
     */
    OpenRole fetchRoleById(String corpid, Long roleid);

    String registryCallback(String corpid, CallbackProperties properties);

    String deleteCallback(String corpid);

    String getFailureCallback(String corpid);

}
