package com.dm.uap.dingtalk.converter;

import org.springframework.stereotype.Component;

import com.dm.dingtalk.api.response.OapiRoleListResponse.OpenRole;
import com.dm.uap.dingtalk.entity.DRole;

@Component
public class DRoleConverter {

	public DRole copyProperties(DRole dRole, OpenRole oRole) {
		dRole.setName(oRole.getName());
		return dRole;
	}

}
