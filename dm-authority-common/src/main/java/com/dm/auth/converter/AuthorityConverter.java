package com.dm.auth.converter;

import java.util.Optional;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dm.auth.dto.AuthorityDto;
import com.dm.auth.dto.MenuAuthorityDto;
import com.dm.auth.dto.ResourceAuthorityDto;
import com.dm.auth.entity.Authority;
import com.dm.common.converter.AbstractConverter;
import com.dm.common.exception.DataNotExistException;

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
//TODO		dto.setRoleName(authority.getRole().getName());
		dto.setRoleId(authority.getId());
		dto.setResourceAuthorities(resourceOperationConverter.toDto(authority.getResourceOperations()));
		return dto;
	}

	public MenuAuthorityDto toMenuAuthorityDto(Authority menuAuthority) {
		MenuAuthorityDto dto = new MenuAuthorityDto();
		dto.setRoleId(menuAuthority.getId());
//TODO		dto.setRoleName(menuAuthority.getRole().getName());
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
