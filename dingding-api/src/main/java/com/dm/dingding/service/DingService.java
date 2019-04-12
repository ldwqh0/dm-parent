package com.dm.dingding.service;

import com.dm.dingding.model.AccessToken;
import com.dm.dingding.model.UserInfo;

public interface DingService {
	public AccessToken getAccessToken();
	
	public UserInfo getUserInfoByUserid(String userid);
}
