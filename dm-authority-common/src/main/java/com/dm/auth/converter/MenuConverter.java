package com.dm.auth.converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.dm.auth.dto.MenuDto;
import com.dm.auth.dto.MenusTreeDto;
import com.dm.auth.entity.Menu;
import com.dm.common.converter.AbstractConverter;

@Component
public class MenuConverter extends AbstractConverter<Menu, MenuDto> {

	public List<MenusTreeDto> toAuthorityMenusDto(List<Menu> menus) {
		Map<Long, MenusTreeDto> authorities = new HashMap<Long, MenusTreeDto>();

		// 将菜单项封装为树形结构
		for (Menu menu : menus) {
			MenusTreeDto menu_ = toAuthorityMenusDto(menu);
			authorities.put(menu_.getId(), menu_);
		}
		List<MenusTreeDto> result = new ArrayList<>();
		for (Menu menu : menus) {
			Menu parentMenu = menu.getParent();
			// 当前菜单项
			MenusTreeDto current = authorities.get(menu.getId());
			if (parentMenu == null) {
				if (!result.contains(current)) {
					result.add(current);
				}
			} else {
				MenusTreeDto parent = authorities.get(parentMenu.getId());
				if (parent == null) {
					// TODO 这里要怎么处理呢？
				} else {
					List<MenusTreeDto> parentSubMenus = parent.getSubmenus();
					parentSubMenus.add(current);
				}
			}
		}
		return result;
	}

	private MenusTreeDto toAuthorityMenusDto(Menu menu) {
		MenusTreeDto dto = new MenusTreeDto();
		dto.setId(menu.getId());
		dto.setDescription(menu.getDescription());
		dto.setName(menu.getName());
		dto.setTitle(menu.getTitle());
		dto.setIcon(menu.getIcon());
		dto.setHref(menu.getUrl());
		dto.setDescription(menu.getDescription());
		dto.setType(menu.getType());
		return dto;
	}

	@Override
	protected MenuDto toDtoActual(Menu model) {
		MenuDto menuDto = new MenuDto();
		menuDto.setId(model.getId());
		menuDto.setName(model.getName());
		menuDto.setTitle(model.getTitle());
		menuDto.setEnabled(model.isEnabled());
		menuDto.setUrl(model.getUrl());
		menuDto.setIcon(model.getIcon());
		if (!Objects.isNull(model.getParent())) {
			menuDto.setParent(toDto(model.getParent()));
		}
		menuDto.setDescription(model.getDescription());
		menuDto.setType(model.getType());
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
		return model;
	}

}
