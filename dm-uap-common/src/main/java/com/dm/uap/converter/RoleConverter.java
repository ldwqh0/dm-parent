package com.dm.uap.converter;

import com.dm.collections.CollectionUtils;
import com.dm.common.converter.Converter;
import com.dm.security.core.userdetails.GrantedAuthorityDto;
import com.dm.uap.dto.RoleDto;
import com.dm.uap.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class RoleConverter implements Converter<Role, RoleDto> {

    private final RoleGroupConverter roleGroupConverter;

    @Autowired
    public RoleConverter(RoleGroupConverter roleGroupConverter) {
        this.roleGroupConverter = roleGroupConverter;
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
        dto.setGroup(roleGroupConverter.toDto(role.getGroup()));
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
        dto.setAuthority(role.getGroup().getName() + "_" + role.getName());
        return dto;
    }

    @Override
    public RoleDto toDto(Role model) {
        return Optional.ofNullable(model).map(this::toDtoActual).orElse(null);
    }

}
