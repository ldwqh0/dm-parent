package com.dm.dingtalk.api.model;

import java.util.HashMap;

public class DingClientConfig extends HashMap<String, String> {

    private static final long serialVersionUID = -3492304173521281382L;

    public DingClientConfig(String appkey, String appsecret, String corpId) {
        super();
        super.put("appkey", appkey);
        super.put("appsecret", appsecret);
        super.put("corpId", corpId);
    }

    public String getAppkey() {
        return get("appkey");
    }

    public String getAppsecret() {
        return get("appsecret");
    }

    public String getCorpId() {
        return get("corpId");
    }

    public void setAppkey(String appKey) {
        put("appkey", appKey);
    }

    public void setAppsecret(String appsecret) {
        put("appsecret", appsecret);
    }

    public void setCorpId(String corpId) {
        put("corpId", corpId);
    }

    public DingClientConfig() {
        super();
    }

}
