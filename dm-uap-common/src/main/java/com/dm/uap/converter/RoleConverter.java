package com.dm.uap.converter;

import com.dm.security.core.userdetails.RoleDto;
import com.dm.uap.entity.UserRole;

import javax.annotation.Nonnull;

public final class RoleConverter {

    public static RoleDto toDto(@Nonnull UserRole model) {
        RoleDto rd = new RoleDto();
        rd.setId(model.getId());
        rd.setName(model.getName());
        rd.setGroup(model.getGroup());
        return rd;
    }
}
