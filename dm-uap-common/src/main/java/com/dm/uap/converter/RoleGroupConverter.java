package com.dm.uap.converter;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.dm.common.converter.Converter;
import com.dm.uap.dto.RoleGroupDto;
import com.dm.uap.entity.RoleGroup;

@Component
public class RoleGroupConverter implements Converter<RoleGroup, RoleGroupDto> {

    private RoleGroupDto toDtoActual(RoleGroup model) {
        RoleGroupDto dto = new RoleGroupDto();
        dto.setId(model.getId());
        dto.setDescription(model.getDescription());
        dto.setName(model.getName());
        return dto;
    }

    @Override
    public RoleGroup copyProperties(RoleGroup model, RoleGroupDto dto) {
        model.setDescription(dto.getDescription());
        model.setName(dto.getName());
        return model;
    }

    @Override
    public RoleGroupDto toDto(RoleGroup model) {
        return Optional.ofNullable(model).map(this::toDtoActual).orElse(null);
    }

}
