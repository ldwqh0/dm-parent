package com.dm.auth.converter;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dm.auth.dto.ResourceOperationDto;
import com.dm.auth.entity.Resource;
import com.dm.auth.entity.ResourceOperation;
import com.dm.common.converter.Converter;

@Component
public class ResourceOperationConverter implements Converter<ResourceOperation, ResourceOperationDto> {

    @Autowired
    private ResourceConverter resourceConverter;

    private ResourceOperationDto toDtoActual(ResourceOperation model) {
        ResourceOperationDto dto = new ResourceOperationDto();
        dto.setSaveable(model.getSaveable());
        dto.setDeleteable(model.getDeleteable());
        dto.setReadable(model.getReadable());
        dto.setResource(resourceConverter.toDto(model.getResource()));
        dto.setUpdateable(model.getUpdateable());
        dto.setPatchable(model.getPatchable());
        return dto;
    }

    @Override
    public ResourceOperation copyProperties(ResourceOperation model, ResourceOperationDto dto) {
        model.setSaveable(dto.getSaveable());
        model.setDeleteable(dto.getDeleteable());
        model.setReadable(dto.getReadable());
        model.setUpdateable(dto.getUpdateable());
        model.setPatchable(dto.getPatchable());
        return model;
    }

    public ResourceOperationDto toDto(Resource resource) {
        ResourceOperationDto dto = new ResourceOperationDto();
        dto.setResource(resourceConverter.toDto(resource));
        return dto;
    }

    @Override
    public ResourceOperationDto toDto(ResourceOperation model) {
        return Optional.ofNullable(model).map(this::toDtoActual).orElse(null);
    }
}
