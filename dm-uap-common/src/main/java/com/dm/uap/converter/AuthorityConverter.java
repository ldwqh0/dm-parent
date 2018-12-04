package com.dm.uap.converter;

import java.util.Optional;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dm.common.converter.AbstractConverter;
import com.dm.common.exception.DataNotExistException;
import com.dm.uap.dto.AuthorityDto;
import com.dm.uap.dto.MenuAuthorityDto;
import com.dm.uap.dto.ResourceAuthorityDto;
import com.dm.uap.entity.Authority;

@Component
public class AuthorityConverter extends AbstractConverter<Authority, AuthorityDto> {

	@Autowired
	private MenuConverter menuConverter;

	@Autowired
	private ResourceOperationConverter resourceOperationConverter;

	@Override
	protected AuthorityDto toDtoActual(Authority model) {
		return null;
	}

	@Override
	public void copyProperties(Authority model, AuthorityDto dto) {
		throw new NotImplementedException("该方法未实现");
	}

	public ResourceAuthorityDto toResourceAuthorityDto(Authority authority) {
		ResourceAuthorityDto dto = new ResourceAuthorityDto();
		dto.setRoleName(authority.getRole().getName());
		dto.setRoleId(authority.getId());
		dto.setResourceAuthorities(resourceOperationConverter.toDto(authority.getResourceOperations()));
		return dto;
	}

	public MenuAuthorityDto toMenuAuthorityDto(Authority menuAuthority) {
		MenuAuthorityDto dto = new MenuAuthorityDto();
		dto.setRoleId(menuAuthority.getId());
		dto.setRoleName(menuAuthority.getRole().getName());
		dto.setAuthorityMenus(menuConverter.toDto(menuAuthority.getMenus()));
		return dto;
	}

	public MenuAuthorityDto toMenuAuthorityDto(Optional<Authority> authority) {
		if (authority.isPresent()) {
			return toMenuAuthorityDto(authority.get());
		} else {
			throw new DataNotExistException();
		}
	}

	public ResourceAuthorityDto toResourceAuthorityDto(Optional<Authority> authority) {
		if (authority.isPresent()) {
			return toResourceAuthorityDto(authority.get());
		} else {
			throw new DataNotExistException();
		}
	}

}
