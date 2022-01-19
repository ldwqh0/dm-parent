package com.dm.auth.converter;

import com.dm.auth.dto.MenuDto;
import com.dm.auth.entity.Menu;

public final class MenuConverter {

    private MenuConverter() {
    }

    private static MenuDto toSimpleDto(Menu model) {
        return new MenuDto(
            model.getId(),
            model.getName(),
            model.getTitle(),
            model.isEnabled(),
            model.getUrl(),
            model.getIcon(),
            model.getDescription(),
            model.getType(),
            model.getOrder(),
            null,
            model.getOpenInNewWindow(),
            null
        );
    }

    public static MenuDto toDto(Menu model, Long childrenCount) {
        return new MenuDto(
            model.getId(),
            model.getName(),
            model.getTitle(),
            model.isEnabled(),
            model.getUrl(),
            model.getIcon(),
            model.getDescription(),
            model.getType(),
            model.getOrder(),
            model.getParent().map(MenuConverter::toSimpleDto).orElse(null),
            model.getOpenInNewWindow(),
            childrenCount
        );
    }
}
