package com.dm.auth.converter;

import com.dm.auth.dto.MenuDto;
import com.dm.auth.entity.Menu;
import com.dm.common.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MenuConverter implements Converter<Menu, MenuDto> {

    private MenuDto toDtoActual(Menu model) {
        MenuDto menuDto = new MenuDto();
        menuDto.setId(model.getId());
        menuDto.setName(model.getName());
        menuDto.setTitle(model.getTitle());
        menuDto.setEnabled(model.isEnabled());
        menuDto.setUrl(model.getUrl());
        menuDto.setIcon(model.getIcon());
        menuDto.setOpenInNewWindow(model.getOpenInNewWindow());
        menuDto.setParent(toDto(model.getParent()));
        menuDto.setDescription(model.getDescription());
        menuDto.setType(model.getType());
        menuDto.setOrder(model.getOrder());
        return menuDto;
    }

    @Override
    public Menu copyProperties(Menu model, MenuDto dto) {
        model.setName(dto.getName());
        model.setTitle(dto.getTitle());
        model.setEnabled(dto.getEnabled());
        model.setUrl(dto.getUrl());
        model.setIcon(dto.getIcon());
        model.setDescription(dto.getDescription());
        model.setType(dto.getType());
        model.setOrder(dto.getOrder());
        model.setOpenInNewWindow(dto.getOpenInNewWindow());
        return model;
    }

    @Override
    public MenuDto toDto(Menu model) {
        return Optional.ofNullable(model).map(this::toDtoActual).orElse(null);
    }
}
