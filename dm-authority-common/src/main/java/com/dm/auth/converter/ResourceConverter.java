package com.dm.auth.converter;

import com.dm.auth.dto.ResourceDto;
import com.dm.auth.entity.AuthResource;
import com.dm.collections.Sets;

public final class ResourceConverter {

    private ResourceConverter() {
    }

    private static ResourceDto.Builder newBuilder(AuthResource model) {
        return ResourceDto.builder()
            .id(model.getId())
            .name(model.getName())
            .methods(model.getMethods())
            .matcher(model.getMatcher())
            .description(model.getDescription())
            .matchType(model.getMatchType());
    }

    public static ResourceDto toSimpleDto(AuthResource model) {
        return newBuilder(model).build();
    }

    public static ResourceDto toDto(AuthResource model) {
        return newBuilder(model)
            .scope(model.getScope())
            .accessAuthorities(Sets.transform(model.getAccessAuthorities(), RoleConverter::toDto))
            .denyAuthorities(Sets.transform(model.getDenyAuthorities(), RoleConverter::toDto))
            .build();
    }
}
