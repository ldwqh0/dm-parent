package com.dm.uap.converter;

import com.dm.common.converter.Converter;
import com.dm.uap.dto.RoleDto;
import com.dm.uap.entity.UserRole;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Component
public class UserRoleConverter implements Converter<UserRole, RoleDto> {

    @Nullable
    @Override
    public RoleDto toDto(@Nonnull UserRole model) {
        RoleDto rd = new RoleDto();
        rd.setId(model.getId());
        rd.setName(model.getName());
        rd.setGroup(model.getGroup());
        return rd;
    }

    @Override
    public UserRole copyProperties(UserRole model, RoleDto dto) {
        model.setId(dto.getId());
        model.setName(dto.getName());
        model.setGroup(dto.getGroup());
        return model;
    }
}
