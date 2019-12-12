package com.dm.uap.dingtalk.converter;

import org.springframework.stereotype.Component;

import com.dm.dingtalk.api.response.OapiRoleListResponse.OpenRoleGroup;
import com.dm.uap.dingtalk.entity.DRoleGroup;

@Component
public class DRoleGroupConverter {

    public DRoleGroup copyProperties(DRoleGroup dRoleGroup, OpenRoleGroup oRoleGroup) {
        dRoleGroup.setName(oRoleGroup.getName());
        return dRoleGroup;
    }

}
