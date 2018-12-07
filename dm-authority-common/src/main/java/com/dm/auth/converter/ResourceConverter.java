package com.dm.auth.converter;

import org.springframework.stereotype.Component;

import com.dm.auth.dto.ResourceDto;
import com.dm.auth.entity.Resource;
import com.dm.common.converter.AbstractConverter;

@Component
public class ResourceConverter extends AbstractConverter<Resource, ResourceDto> {

	@Override
	protected ResourceDto toDtoActual(Resource model) {
		ResourceDto dto = new ResourceDto();
		dto.setId(model.getId());
		dto.setName(model.getName());
		dto.setMatcher(model.getMatcher());
		dto.setDescription(model.getDescription());
		dto.setMatchType(model.getMatchType());
		return dto;
	}

	@Override
	public void copyProperties(Resource model, ResourceDto dto) {
		model.setMatcher(dto.getMatcher());
		model.setDescription(dto.getDescription());
		model.setName(dto.getName());
		model.setMatchType(dto.getMatchType());
	}

}
