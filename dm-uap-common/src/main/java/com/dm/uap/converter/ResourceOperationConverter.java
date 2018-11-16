package com.dm.uap.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dm.common.converter.AbstractConverter;
import com.dm.uap.dto.ResourceOperationDto;
import com.dm.uap.entity.ResourceOperation;

@Component
public class ResourceOperationConverter extends AbstractConverter<ResourceOperation, ResourceOperationDto> {

	@Autowired
	private ResourceConverter resourceConverter;

	@Override
	protected ResourceOperationDto toDtoActual(ResourceOperation model) {
		ResourceOperationDto dto = new ResourceOperationDto();
		dto.setSaveable(model.isSaveable());
		dto.setDeleteable(model.isDeleteable());
		dto.setReadable(model.isReadable());
		dto.setResource(resourceConverter.toDto(model.getResource()));
		dto.setUpdateable(model.isUpdateable());
		return dto;
	}

	@Override
	public void copyProperties(ResourceOperation model, ResourceOperationDto dto) {
		model.setSaveable(dto.isSaveable());
		model.setDeleteable(dto.isDeleteable());
		model.setReadable(dto.isReadable());
		model.setUpdateable(dto.isUpdateable());
	}

}
