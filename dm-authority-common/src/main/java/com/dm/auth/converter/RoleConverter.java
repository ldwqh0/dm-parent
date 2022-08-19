package com.dm.auth.converter;

import com.dm.auth.dto.MenuAuthorityDto;
import com.dm.auth.dto.RoleDto;
import com.dm.auth.entity.Role;
import com.dm.collections.CollectionUtils;
import com.dm.collections.Sets;
import com.dm.security.core.userdetails.GrantedAuthorityDto;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class RoleConverter {

    private RoleConverter() {

    }

    public static List<? extends GrantedAuthorityDto> toGrantedAuthorityDto(List<Role> roles) {
        if (CollectionUtils.isNotEmpty(roles)) {
            return roles.stream().map(RoleConverter::toGrantedAuthorityDto).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    public static GrantedAuthorityDto toGrantedAuthorityDto(Role role) {
        return GrantedAuthorityDto.of(role.getId(), role.getGroup() + "_" + role.getName());
    }

    public static RoleDto toDto(Role model) {
        return new RoleDto(model.getId(), model.getName(), model.getGroup(), model.getDescription(), model.getState());
    }

    public static MenuAuthorityDto toMenuAuthorityDto(Role role) {
        return new MenuAuthorityDto(role.getId(), role.getName(), Sets.transform(role.getMenus(), it -> MenuConverter.toDto(it, null)));
    }
}
