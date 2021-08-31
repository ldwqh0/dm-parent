package com.dm.uap.converter;

import com.dm.security.core.userdetails.GrantedAuthorityDto;
import com.dm.uap.dto.UserRoleDto;
import com.dm.uap.entity.UserRole;
import org.springframework.security.core.GrantedAuthority;

import javax.annotation.Nonnull;

public final class RoleConverter {

    public static UserRoleDto toDto(@Nonnull UserRole model) {
        UserRoleDto rd = new UserRoleDto();
        rd.setId(model.getId());
        rd.setName(model.getName());
        rd.setGroup(model.getGroup());
        return rd;
    }

    public static GrantedAuthority toGrantedAuthority(UserRole model) {
        return new GrantedAuthorityDto(
            model.getGroup() + "_" + model.getName(),
            model.getId()
        );
    }
}
