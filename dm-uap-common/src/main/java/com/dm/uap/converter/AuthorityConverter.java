package com.dm.uap.converter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dm.common.converter.AbstractConverter;
import com.dm.uap.dto.AuthorityDto;
import com.dm.uap.entity.Authority;
import com.dm.uap.entity.Menu;

@Component
public class AuthorityConverter extends AbstractConverter<Authority, AuthorityDto> {

	@Autowired
	private MenuConverter menuConverter;

	@Override
	protected AuthorityDto toDtoActual(Authority model) {
		AuthorityDto dto = new AuthorityDto();
		dto.setRoleId(model.getId());
		List<Menu> menus = model.getMenus();
		dto.setAuthorityMenus(menuConverter.toDto(menus));
		return dto;
	}

	@Override
	public void copyProperties(Authority model, AuthorityDto dto) {
		// TODO Auto-generated method stub

	}

}
