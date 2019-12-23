package com.dm.auth.converter;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.dm.auth.dto.ResourceDto;
import com.dm.auth.entity.Resource;
import com.dm.common.converter.Converter;

@Component
public class ResourceConverter implements Converter<Resource, ResourceDto> {
    private ResourceDto toDtoActual(Resource model) {
        ResourceDto dto = new ResourceDto();
        dto.setId(model.getId());
        dto.setName(model.getName());
        dto.setMatcher(model.getMatcher());
        dto.setDescription(model.getDescription());
        dto.setMatchType(model.getMatchType());
        dto.setScope(model.getScope());
        return dto;
    }

    @Override
    public Resource copyProperties(Resource model, ResourceDto dto) {
        model.setMatcher(dto.getMatcher());
        model.setDescription(dto.getDescription());
        model.setName(dto.getName());
        model.setMatchType(dto.getMatchType());
        model.setScope(dto.getScope());
        return model;
    }

    @Override
    public ResourceDto toDto(Resource model) {
        return Optional.ofNullable(model).map(this::toDtoActual).orElse(null);
    }

}
