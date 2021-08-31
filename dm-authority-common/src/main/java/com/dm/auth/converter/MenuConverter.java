package com.dm.auth.converter;

import com.dm.auth.dto.MenuDto;
import com.dm.auth.entity.Menu;

import java.util.Optional;

public final class MenuConverter {

    private MenuConverter() {
    }

    public static MenuDto toDto(Menu model) {
        return Optional.ofNullable(model).map(
            it -> {
                MenuDto menuDto = new MenuDto();
                menuDto.setId(it.getId());
                menuDto.setName(it.getName());
                menuDto.setTitle(it.getTitle());
                menuDto.setEnabled(it.isEnabled());
                menuDto.setUrl(it.getUrl());
                menuDto.setIcon(it.getIcon());
                menuDto.setOpenInNewWindow(it.getOpenInNewWindow());
                menuDto.setParent(toDto(it.getParent()));
                menuDto.setDescription(it.getDescription());
                menuDto.setType(it.getType());
                menuDto.setOrder(it.getOrder());
                return menuDto;
            }
        ).orElse(null);
    }
}
