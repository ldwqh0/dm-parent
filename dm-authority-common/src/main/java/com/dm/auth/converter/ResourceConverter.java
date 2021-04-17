package com.dm.auth.converter;

import com.dm.auth.dto.ResourceDto;
import com.dm.auth.entity.AuthResource;
import com.dm.collections.Sets;
import com.dm.common.converter.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ResourceConverter implements Converter<AuthResource, ResourceDto> {

    private final RoleConverter roleConverter;

    private ResourceDto toDtoActual(AuthResource model) {
        ResourceDto dto = new ResourceDto();
        dto.setId(model.getId());
        dto.setName(model.getName());
        dto.setMatcher(model.getMatcher());
        dto.setDescription(model.getDescription());
        dto.setMatchType(model.getMatchType());
        dto.setScope(model.getScope());
        dto.setMethods(model.getMethods());
        dto.setDenyAll(model.isDenyAll());
        dto.setAuthenticated(model.isAuthenticated());
        dto.setAccessAuthorities(Sets.transform(model.getAccessAuthorities(), roleConverter::toDto));
        dto.setDenyAuthorities(Sets.transform(model.getDenyAuthorities(), roleConverter::toDto));
        return dto;
    }

    @Override
    public AuthResource copyProperties(AuthResource model, ResourceDto dto) {
        model.setMatcher(dto.getMatcher());
        model.setDescription(dto.getDescription());
        model.setName(dto.getName());
        model.setMatchType(dto.getMatchType());
        model.setScope(dto.getScope());
        model.setDenyAll(dto.isDenyAll());
        model.setMethods(dto.getMethods());
        model.setAuthenticated(dto.isAuthenticated());
        return model;
    }

    @Override
    public ResourceDto toDto(AuthResource model) {
        return Optional.ofNullable(model).map(this::toDtoActual).orElse(null);
    }

}
