package com.dm.dingtalk.api.service.impl;

import java.time.ZonedDateTime;
import java.util.Objects;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.client.RestTemplate;

import com.dm.dingtalk.api.response.AccessTokenResponse;
import com.dm.dingtalk.api.service.DingtalkAccessTokenService;

/**
 * 企业内部应用token服务
 **/
public class OrganizationInternalAppAccessTokenService implements DingtalkAccessTokenService, InitializingBean {

    private RestTemplate restTemplate = null;

    private final Object tokenLock = new Object();

    private volatile AccessTokenResponse existToken;

    private final String appkey;

    private final String appsecret;

    private final String corpid;

    private static final String SERVER = "https://oapi.dingtalk.com";

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * 使用必要参数构建一个 {@link OrganizationInternalAppAccessTokenService}
     * 
     * @param appkey    应用ID
     * @param appsecret 应用密钥
     * @param corpid    企业编号
     */
    public OrganizationInternalAppAccessTokenService(String appkey, String appsecret, String corpid) {
        this.appkey = appkey;
        this.appsecret = appsecret;
        this.corpid = corpid;
    }

    @Override
    public String getAccessToken(String corpid) {
        if (!validateAccessToken(this.existToken)) {
            synchronized (tokenLock) {
                if (!validateAccessToken(existToken)) {
                    existToken = getAccessTokenFromServer();
                }
            }
        }
        return existToken.getAccessToken();
    }

    /**
     * 从服务器获取AccessToken
     * 
     * @return
     */
    private AccessTokenResponse getAccessTokenFromServer() {
        String url = SERVER + "/gettoken?appkey={0}&appsecret={1}";
        AccessTokenResponse response = restTemplate.getForObject(url, AccessTokenResponse.class, appkey, appsecret);
        response.setCropId(this.corpid);
        return response;
    }

    /**
     * 校验token是否过期
     * 
     * @param token
     * @return
     */
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
    public void afterPropertiesSet() throws Exception {
        if (Objects.isNull(restTemplate)) {
            this.restTemplate = new RestTemplate();
        }
    }

}
