package com.dm.uap.converter;

//import com.dm.security.core.userdetails.GrantedAuthorityDto;

import com.dm.uap.dto.UserRoleDto;
import com.dm.uap.entity.UserRole;

public final class RoleConverter {

    public static UserRoleDto toDto(UserRole model) {
        return new UserRoleDto(
            model.getId(),
            model.getGroup(),
            model.getName()
        );
    }

}
