package com.dm.auth.converter;

import com.dm.auth.dto.MenuAuthorityDto;
import com.dm.auth.dto.RoleDto;
import com.dm.auth.entity.Role;
import com.dm.collections.CollectionUtils;
import com.dm.collections.Sets;
import com.dm.security.core.userdetails.GrantedAuthorityDto;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
        GrantedAuthorityDto dto = new GrantedAuthorityDto();
        dto.setId(role.getId());
        dto.setAuthority(role.getGroup() + "_" + role.getName());
        return dto;
    }

    public static RoleDto toDto(Role model) {
        return Optional.ofNullable(model).map(role -> {
            RoleDto dto = new RoleDto();
            dto.setId(role.getId());
            dto.setName(role.getName());
            dto.setState(role.getState());
            dto.setDescription(role.getDescription());
            dto.setGroup(role.getGroup());
            // 角色信息不包括用户信息
            // dto.setUsers(userConverter.toDto(role.getUsers()));
            return dto;
        }).orElse(null);
    }

    public static MenuAuthorityDto toMenuAuthorityDto(Role role) {
        MenuAuthorityDto dto = new MenuAuthorityDto();
        dto.setRoleName(role.getName());
        dto.setRoleId(role.getId());
        dto.setAuthorityMenus(Sets.transform(role.getMenus(), MenuConverter::toDto));
        return dto;
    }

}
