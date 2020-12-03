package com.dm.auth.converter;

import com.dm.auth.dto.MenuAuthorityDto;
import com.dm.auth.dto.ResourceAuthorityDto;
import com.dm.auth.dto.RoleDto;
import com.dm.auth.entity.AuthResource;
import com.dm.auth.entity.Role;
import com.dm.collections.CollectionUtils;
import com.dm.collections.Maps;
import com.dm.collections.Sets;
import com.dm.common.converter.Converter;
import com.dm.security.core.userdetails.GrantedAuthorityDto;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class RoleConverter implements Converter<Role, RoleDto> {
    
    private final MenuConverter menuConverter;

    public RoleConverter(MenuConverter menuConverter) {
        this.menuConverter = menuConverter;
    }

    /**
     * 转换为简单的角色数据对象，不包含用户信息
     *
     * @param role 要转的角色信息
     * @return 角色信息，不包括角色的用户等关联信息
     */
    private RoleDto toDtoActual(Role role) {
        RoleDto dto = new RoleDto();
        dto.setId(role.getId());
        dto.setName(role.getName());
        dto.setState(role.getState());
        dto.setDescription(role.getDescription());
        dto.setGroup(role.getGroup());
        // 角色信息不包括用户信息
        // dto.setUsers(userConverter.toDto(role.getUsers()));
        return dto;
    }

    @Override
    public Role copyProperties(Role role, RoleDto roleDto) {
        if (role != null && roleDto != null) {
            role.setName(roleDto.getName());
            role.setDescription(roleDto.getDescription());
            role.setState(roleDto.getState());
            role.setGroup(roleDto.getGroup());
        }
        return role;
    }

    public List<? extends GrantedAuthorityDto> toGrantedAuthorityDto(List<Role> roles) {
        if (CollectionUtils.isNotEmpty(roles)) {
            return roles.stream().map(this::toGrantedAuthorityDto).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    public GrantedAuthorityDto toGrantedAuthorityDto(Role role) {
        GrantedAuthorityDto dto = new GrantedAuthorityDto();
        dto.setId(role.getId());
        dto.setAuthority(role.getGroup() + "_" + role.getName());
        return dto;
    }

    @Override
    public RoleDto toDto(Role model) {
        return Optional.ofNullable(model).map(this::toDtoActual).orElse(null);
    }
    
    public ResourceAuthorityDto toResourceAuthorityDto(Role role) {
        ResourceAuthorityDto dto = new ResourceAuthorityDto();
        dto.setRoleName(role.getName());
        dto.setResourceAuthorities(Maps.transformKeys(role.getResourceOperations(), AuthResource::getId));
        return dto;
    }

    public MenuAuthorityDto toMenuAuthorityDto(Role role) {
        MenuAuthorityDto dto = new MenuAuthorityDto();
        dto.setRoleName(role.getName());
        dto.setRoleId(role.getId());
        dto.setAuthorityMenus(Sets.transform(role.getMenus(), menuConverter::toDto));
        return dto;
    }


}
