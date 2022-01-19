package com.dm.auth.converter;

import com.dm.auth.dto.ResourceDto;
import com.dm.auth.entity.AuthResource;
import com.dm.collections.Sets;

public final class ResourceConverter {

    private ResourceConverter() {
    }

    public static ResourceDto toSimpleDto(AuthResource model) {
        return new ResourceDto(
            model.getId(),
            model.getName(),
            model.getMethods(),
            model.getMatcher(),
            model.getDescription(),
            null,
            model.getMatchType(),
            null,
            null
        );
    }


    public static ResourceDto toDto(AuthResource model) {
        return new ResourceDto(
            model.getId(),
            model.getName(),
            model.getMethods(),
            model.getMatcher(),
            model.getDescription(),
            model.getScope(),
            model.getMatchType(),
            Sets.transform(model.getAccessAuthorities(), RoleConverter::toDto),
            Sets.transform(model.getDenyAuthorities(), RoleConverter::toDto)
        );
    }
}
