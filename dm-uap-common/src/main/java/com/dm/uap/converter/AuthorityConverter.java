package com.dm.uap.converter;

import java.util.Optional;
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
		// TODO Auto-generated method stub

	}

	public ResourceAuthorityDto toResourceAuthorityDto(Authority authority) {
		ResourceAuthorityDto dto = new ResourceAuthorityDto();
		dto.setRoleId(authority.getId());
		dto.setResourceAuthorities(resourceOperationConverter.toDto(authority.getResourceOperations()));
		return dto;
	}

	public MenuAuthorityDto toMenuAuthorityDto(Authority menuAuthority) {
		// TODO Auto-generated method stub
		return null;
	}

	public MenuAuthorityDto toMenuAuthorityDto(Optional<Authority> authority) {
		// TODO Auto-generated method stub
		return null;
	}

	public ResourceAuthorityDto toResourceAuthorityDto(Optional<Authority> authority) {
		if (authority.isPresent()) {
			return toResourceAuthorityDto(authority.get());
		} else {
			throw new DataNotExistException();
		}
	}

}
