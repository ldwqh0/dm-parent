package com.dm.auth.converter;

import com.dm.auth.dto.ResourceDto;
import com.dm.auth.entity.AuthResource;
import com.dm.collections.Sets;

import java.util.Optional;

public final class ResourceConverter {

    private ResourceConverter() {
    }

    public static ResourceDto toListDto(AuthResource model) {
        ResourceDto dto = new ResourceDto();
        dto.setId(model.getId());
        dto.setName(model.getName());
        dto.setMatcher(model.getMatcher());
        dto.setDescription(model.getDescription());
        dto.setMatchType(model.getMatchType());
        dto.setMethods(Sets.hashSet(model.getMethods()));
        return dto;
    }


    public static ResourceDto toDto(AuthResource model) {
        return Optional.ofNullable(model).map(it -> {
            ResourceDto dto = toListDto(it);
            dto.setScope(Sets.hashSet(it.getScope()));
            dto.setAccessAuthorities(Sets.transform(it.getAccessAuthorities(), RoleConverter::toDto));
            dto.setDenyAuthorities(Sets.transform(it.getDenyAuthorities(), RoleConverter::toDto));
            return dto;
        }).orElse(null);
    }

}
