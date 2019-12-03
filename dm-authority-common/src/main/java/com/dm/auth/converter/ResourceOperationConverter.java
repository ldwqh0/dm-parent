package com.dm.auth.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dm.auth.dto.ResourceOperationDto;
import com.dm.auth.entity.Resource;
import com.dm.auth.entity.ResourceOperation;
import com.dm.common.converter.AbstractConverter;

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
        dto.setResource(resourceConverter.toDto(model.getResource()).orElse(null));
        dto.setUpdateable(model.getUpdateable());
        return dto;
    }

    @Override
    public ResourceOperation copyProperties(ResourceOperation model, ResourceOperationDto dto) {
        model.setSaveable(dto.getSaveable());
        model.setDeleteable(dto.getDeleteable());
        model.setReadable(dto.getReadable());
        model.setUpdateable(dto.getUpdateable());
        return model;
    }

    public ResourceOperationDto toDto(Resource resource) {
        ResourceOperationDto dto = new ResourceOperationDto();
        dto.setResource(resourceConverter.toDto(resource).orElse(null));
        return dto;
    }
}
