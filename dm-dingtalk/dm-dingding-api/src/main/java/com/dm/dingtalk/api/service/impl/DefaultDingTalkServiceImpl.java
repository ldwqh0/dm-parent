package com.dm.dingtalk.api.service.impl;

import com.dm.collections.CollectionUtils;
import com.dm.dingtalk.api.callback.CallbackProperties;
import com.dm.dingtalk.api.request.*;
import com.dm.dingtalk.api.response.*;
import com.dm.dingtalk.api.response.OapiDepartmentListResponse.Department;
import com.dm.dingtalk.api.response.OapiRoleListResponse.OpenRoleGroup;
import com.dm.dingtalk.api.service.DingTalkService;
import com.dm.dingtalk.api.service.DingtalkAccessTokenService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
public class DefaultDingTalkServiceImpl implements DingTalkService, InitializingBean {
    private static final String SERVER = "https://oapi.dingtalk.com";

    private RestTemplate restTemplate;

    private static final Long USER_NOT_FOUND_CODE = 60121L;

    private DingtalkAccessTokenService accessTokenService;

    public void setDingtalkAccessTokenService(DingtalkAccessTokenService accessTokenService) {
        this.accessTokenService = accessTokenService;
    }

    public DefaultDingTalkServiceImpl() {
        super();
    }

    @Autowired(required = false)
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * 获取指定企业的部门列表
     */
    @Override
    public List<Department> fetchDepartments(String corpid) {
        String url = SERVER + "/department/list?access_token={0}";
        OapiDepartmentListResponse response = restTemplate.getForObject(url, OapiDepartmentListResponse.class,
            accessTokenService.getAccessToken(corpid));
        checkResponse(response);
        if (Objects.isNull(response) || CollectionUtils.isEmpty(response.getDepartment())) {
            return Collections.emptyList();
        } else {
            List<Department> departments = response.getDepartment();
            departments.forEach(department -> department.setCorpId(corpid));
            return departments;
        }
    }

    @Override
    public List<OpenRoleGroup> fetchRoleGroups(String corpid) {
        String url = SERVER + "/topapi/role/list?access_token={0}";
        OapiRoleListResponse response = restTemplate.getForObject(url, OapiRoleListResponse.class,
            accessTokenService.getAccessToken(corpid));
        checkResponse(response);
        if (Objects.nonNull(response) && Objects.nonNull(response.getResult())
            && CollectionUtils.isNotEmpty(response.getResult().getList())) {
            List<OpenRoleGroup> groups = response.getResult().getList();
            groups.forEach(i -> i.setCorpid(corpid));
            return groups;
        } else {
            return Collections.emptyList();
        }

    }

    @Override
    public OapiUserCreateResponse createUser(String corpid, OapiUserCreateRequest request) {
        String url = SERVER + "/user/create?access_token={0}";
        OapiUserCreateResponse response = restTemplate.postForObject(url, request, OapiUserCreateResponse.class,
            accessTokenService.getAccessToken(corpid));
        // 创建用户，如果用户已经存在于钉钉系统中了,不会做任何修改，但会返回返回已经存在的用户的userid
        if (Objects.nonNull(response) && StringUtils.isNotEmpty(response.getUserid())) {
        } else {
            checkResponse(response);
        }
        return response;
    }

    @Override
    public OapiUserUpdateResponse updateUser(String corpid, OapiUserUpdateRequest request) {
        String url = SERVER + "/user/update?access_token={0}";
        OapiUserUpdateResponse response = restTemplate.postForObject(url, request, OapiUserUpdateResponse.class,
            accessTokenService.getAccessToken(corpid));
        checkResponse(response);
        return response;
    }

    @Override
    public OapiUserGetuserinfoResponse getUserByAuthCode(String corpid, String authCode) {
        String url = SERVER + "/user/getuserinfo?access_token={0}&code={1}";
        OapiUserGetuserinfoResponse response = restTemplate.getForObject(url, OapiUserGetuserinfoResponse.class,
            accessTokenService.getAccessToken(corpid), authCode);
        response.setCorpid(corpid);
        checkResponse(response);
        return response;
    }

    /**
     * 获取部门的用户信息
     */
    @Override
    // 只能单线程获取用户
    public OapiUserGetDeptMemberResponse fetchUsers(String corpid, Long depId) {
        log.info("获取部门的用户信息在线程{}", +Thread.currentThread().getId());
        String url = SERVER + "/user/getDeptMember?access_token={0}&deptId={1}";
        OapiUserGetDeptMemberResponse response = restTemplate.getForObject(url, OapiUserGetDeptMemberResponse.class,
            accessTokenService.getAccessToken(corpid), depId);
        checkResponse(response);
        return response;
    }

    @Override
    public OapiUserGetResponse fetchUserById(String corpid, String userid) {
        String url = SERVER + "/user/get?access_token={0}&userid={1}";
        OapiUserGetResponse response = restTemplate.getForObject(url, OapiUserGetResponse.class,
            accessTokenService.getAccessToken(corpid),
            userid);
        if (Objects.isNull(response)) {
            throw new RuntimeException("the response is null");
        } else if (Objects.equals(USER_NOT_FOUND_CODE, response.getErrcode())) {
            return response;
        } else {
            checkResponse(response);
            return response;
        }
    }

    @Override
    public OapiRoleAddrolesforempsResponse batchSetUserRole(String corpid, Collection<String> userIds,
                                                            Collection<Long> roleIds) {
        if (CollectionUtils.isNotEmpty(roleIds)) {
            String url = SERVER + "/topapi/role/addrolesforemps?access_token={0}";
            OapiRoleAddrolesforempsRequest request = new OapiRoleAddrolesforempsRequest(userIds, roleIds);
            OapiRoleAddrolesforempsResponse response = restTemplate.postForObject(url, request,
                OapiRoleAddrolesforempsResponse.class, accessTokenService.getAccessToken(corpid));
            checkResponse(response);
            return response;
        } else {
            return null;
        }
    }

    /**
     * 校验响应是否正确
     */
    private void checkResponse(TaobaoResponse response) {
        if (Objects.isNull(response)) {
            throw new RuntimeException("the response is null");
        } else if (!response.isSuccess()) {
            log.error("响应校验错误，{" + response.getErrmsg() + "}");
            throw new RuntimeException(response.getErrmsg());
        }

    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(accessTokenService, "the dingtalk access token service can not be null!");
        // 配置一个默认的restTemplate
        if (Objects.isNull(restTemplate)) {
            this.restTemplate = new RestTemplate();
        }
    }

    @Override
    public void deleteUser(String corpid, String userid) {
        String url = SERVER + "/user/delete?access_token={0}&userid={1}";
        OapiUserDeleteResponse response = restTemplate.getForObject(url, OapiUserDeleteResponse.class,
            accessTokenService.getAccessToken(corpid),
            userid);
        checkResponse(response);
    }

    @Override
    public OapiWorkrecordAddResponse addWorkRecord(String corpid, OapiWorkrecordAddRequest request) {
        String url = SERVER + "/topapi/workrecord/add?access_token={0}";
        OapiWorkrecordAddResponse response = restTemplate.postForObject(url, request, OapiWorkrecordAddResponse.class,
            accessTokenService.getAccessToken(corpid));
        checkResponse(response);
        return response;
    }

    @Override
    public OapiWorkrecordUpdateResponse updateWorkRecord(String corpid, OapiWorkrecordUpdateRequest request) {
        String url = SERVER + "/topapi/workrecord/update?access_token={0}";
        OapiWorkrecordUpdateResponse response = restTemplate.postForObject(url, request,
            OapiWorkrecordUpdateResponse.class,
            accessTokenService.getAccessToken(corpid));
        checkResponse(response);
        return response;
    }

    @Override
    public OapiWorkrecordGetbyuseridResponse getWorkRecordByUserid(String corpid,
                                                                   OapiWorkrecordGetbyuseridRequest request) {
        String url = SERVER + "/topapi/workrecord/getbyuserid?access_token={0}";
        OapiWorkrecordGetbyuseridResponse response = restTemplate.postForObject(url, request,
            OapiWorkrecordGetbyuseridResponse.class, accessTokenService.getAccessToken(corpid));
        checkResponse(response);
        return response;
    }

    @Override
    public String registryCallback(String corpid, CallbackProperties properties) {
        String url = SERVER + "/call_back/register_call_back?access_token={0}";
        return restTemplate.postForObject(url, properties, String.class, accessTokenService.getAccessToken(corpid));
    }

    @Override
    public String deleteCallback(String corpid) {
        String url = SERVER + "/call_back/delete_call_back?access_token={0}";
        return restTemplate.getForObject(url, String.class, accessTokenService.getAccessToken(corpid));
    }

    @Override
    public String getFailureCallback(String corpid) {
        String url = SERVER + "/call_back/get_call_back_failed_result?access_token={0}";
        return restTemplate.getForObject(url, String.class, accessTokenService.getAccessToken(corpid));
    }

    @Override
    public OpenRole fetchRoleById(String corpid, Long roleid) {
        String url = SERVER + "/topapi/role/getrole?access_token={0}";
        Map<String, Object> request = new HashMap<>();
        request.put("roleId", roleid);
        OapiRoleGetroleResponse rsp = restTemplate.postForObject(url, request, OapiRoleGetroleResponse.class,
            accessTokenService.getAccessToken(corpid));
        checkResponse(rsp);
        OpenRole or = rsp.getRole();
        or.setId(roleid);
        or.setCorpId(corpid);
        return or;
    }

}
