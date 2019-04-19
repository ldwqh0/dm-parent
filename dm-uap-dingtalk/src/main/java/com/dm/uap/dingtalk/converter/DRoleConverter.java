package com.dm.uap.dingtalk.converter;

import org.springframework.stereotype.Component;

import com.dm.dingtalk.api.response.OapiRoleListResponse.OpenRole;
import com.dm.uap.dingtalk.entity.DRole;

@Component
public class DRoleConverter {

	public void copyProperties(DRole dRole, OpenRole oRole) {
		dRole.setId(oRole.getId());
		dRole.setName(oRole.getName());
	}

}
