package com.dm.uap.converter;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dm.common.converter.AbstractConverter;
import com.dm.uap.dto.AuthorityDto;
import com.dm.uap.dto.MenuAuthorityDto;
import com.dm.uap.dto.ResourceAuthorityDto;
import com.dm.uap.entity.Authority;
import com.dm.uap.entity.Menu;

@Component
public class AuthorityConverter extends AbstractConverter<Authority, AuthorityDto> {

	@Autowired
	private MenuConverter menuConverter;

	@Override
	protected AuthorityDto toDtoActual(Authority model) {
		return null;
	}

	public ResourceAuthorityDto toResourceAuthorityDto(Authority authority) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void copyProperties(Authority model, AuthorityDto dto) {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub
		return null;
	}

}
