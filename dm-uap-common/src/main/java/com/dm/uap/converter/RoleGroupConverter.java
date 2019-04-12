package com.dm.uap.converter;

import org.springframework.stereotype.Component;

import com.dm.common.converter.AbstractConverter;
import com.dm.uap.dto.RoleGroupDto;
import com.dm.uap.entity.RoleGroup;

@Component
public class RoleGroupConverter extends AbstractConverter<RoleGroup, RoleGroupDto> {

	@Override
	protected RoleGroupDto toDtoActual(RoleGroup model) {
		RoleGroupDto dto = new RoleGroupDto();
		dto.setId(model.getId());
		dto.setDescription(model.getDescription());
		dto.setName(model.getName());
		return dto;
	}

	@Override
	public void copyProperties(RoleGroup model, RoleGroupDto dto) {
		model.setDescription(dto.getDescription());
		model.setName(dto.getName());
	}

}
