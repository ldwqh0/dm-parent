package com.dm.auth.converter;

import com.dm.auth.dto.ResourceDto;
import com.dm.auth.entity.AuthResource;
import com.dm.collections.Sets;

public final class ResourceConverter {

    private ResourceConverter() {
    }

    public static ResourceDto toSimpleDto(AuthResource model) {
        ResourceDto dto = new ResourceDto();
        dto.setId(model.getId());
        dto.setName(model.getName());
        dto.setMatcher(model.getMatcher());
        dto.setDescription(model.getDescription());
        dto.setMatchType(model.getMatchType());
        return dto;
    }


    public static ResourceDto toDto(AuthResource model) {
        ResourceDto dto = toSimpleDto(model);
        dto.setScope(model.getScope());
        dto.setMethods(model.getMethods());
        dto.setAccessAuthorities(Sets.transform(model.getAccessAuthorities(), RoleConverter::toDto));
        dto.setDenyAuthorities(Sets.transform(model.getDenyAuthorities(), RoleConverter::toDto));
        return dto;
    }

}
