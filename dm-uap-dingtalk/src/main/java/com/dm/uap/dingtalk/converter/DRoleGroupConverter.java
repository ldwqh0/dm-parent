package com.dm.uap.dingtalk.converter;

import org.springframework.stereotype.Component;

import com.dm.dingtalk.api.response.OapiRoleListResponse.OpenRoleGroup;
import com.dm.uap.dingtalk.entity.DRoleGroup;

@Component
public class DRoleGroupConverter {

	public void copyProperties(DRoleGroup dRoleGroup, OpenRoleGroup oRoleGroup) {
		dRoleGroup.setId(oRoleGroup.getGroupId());
		dRoleGroup.setName(oRoleGroup.getName());
	}

}
