package com.dm.dingtalk.api.service;

public interface DingtalkAccessTokenService {

    /**
     * 获取指定企业的access_token
     * 
     * @param corpid
     * @return
     */
    public String getAccessToken(String corpid);

}
