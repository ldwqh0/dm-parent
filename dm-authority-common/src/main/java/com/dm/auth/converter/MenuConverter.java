package com.dm.auth.converter;

import com.dm.auth.dto.MenuDto;
import com.dm.auth.entity.Menu;

public final class MenuConverter {

    private MenuConverter() {
    }

    private static MenuDto.Builder newBuilder(Menu model) {
        MenuDto.Builder builder = MenuDto.builder()
            .id(model.getId())
            .name(model.getName())
            .title(model.getTitle())
            .enabled(model.isEnabled())
            .url(model.getUrl())
            .icon(model.getIcon())
            .description(model.getDescription())
            .type(model.getType())
            .order(model.getOrder())
            .openInNewWindow(model.isOpenInNewWindow());
        model.getParent().map(MenuConverter::toSimpleDto)
            .ifPresent(builder::parent);
        return builder;
    }

    private static MenuDto toSimpleDto(Menu model) {
        return newBuilder(model).build();
    }

    public static MenuDto toDto(Menu model, Integer childrenCount) {
        return newBuilder(model).childrenCount(childrenCount).build();
    }
}
