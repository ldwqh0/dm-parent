package com.dm.uap.dingtalk.converter;

import org.springframework.stereotype.Component;

import com.dm.auth.entity.Role;
import com.dm.dingtalk.api.response.OpenRole;
import com.dm.uap.dingtalk.entity.DRole;

@Component
public class DRoleConverter {

    public DRole copyProperties(DRole dRole, OpenRole oRole) {
        dRole.setName(oRole.getName());
        return dRole;
    }

    public Role copyProperties(Role role, DRole dRole) {
        role.setName(dRole.getName());
        return role;
    }

}
