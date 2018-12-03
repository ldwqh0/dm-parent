package com.dm.uap.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dm.common.converter.AbstractConverter;
import com.dm.uap.dto.ResourceOperationDto;
import com.dm.uap.entity.Resource;
import com.dm.uap.entity.ResourceOperation;

@Component
public class ResourceOperationConverter extends AbstractConverter<ResourceOperation, ResourceOperationDto> {

	@Autowired
	private ResourceConverter resourceConverter;

	@Override
	protected ResourceOperationDto toDtoActual(ResourceOperation model) {
		ResourceOperationDto dto = new ResourceOperationDto();
		dto.setSaveable(model.getSaveable());
		dto.setDeleteable(model.getDeleteable());
		dto.setReadable(model.getReadable());
		dto.setResource(resourceConverter.toDto(model.getResource()));
		dto.setUpdateable(model.getUpdateable());
		return dto;
	}

	@Override
	public void copyProperties(ResourceOperation model, ResourceOperationDto dto) {
		model.setSaveable(dto.getSaveable());
		model.setDeleteable(dto.getDeleteable());
		model.setReadable(dto.getReadable());
		model.setUpdateable(dto.getUpdateable());
	}

	public ResourceOperationDto toDto(Resource resource) {
		ResourceOperationDto dto = new ResourceOperationDto();
		dto.setResource(resourceConverter.toDto(resource));
		return dto;
	}
}
