package com.dm.dingding.model;

import java.util.HashMap;

public class DingClientConfig extends HashMap<String, String> {

	private static final long serialVersionUID = -3492304173521281382L;

	public DingClientConfig(String appkey, String appsecret) {
		super();
		super.put("appkey", appkey);
		super.put("appsecret", appsecret);
	}

	public String getAppkey() {
		return get("appkey");
	}

	public String getAppsecret() {
		return get("appsecret");
	}

	public void setAppkey(String appKey) {
		put("appkey", appKey);
	}

	public void setAppsecret(String appsecret) {
		put("appsecret", appsecret);
	}

	public DingClientConfig() {
		super();
	}

}
