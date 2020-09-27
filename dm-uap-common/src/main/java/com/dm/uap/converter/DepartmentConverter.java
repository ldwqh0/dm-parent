package com.dm.uap.converter;

import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.dm.common.converter.Converter;
import com.dm.uap.dto.DepartmentDto;
import com.dm.uap.entity.Department;

@Component
public class DepartmentConverter implements Converter<Department, DepartmentDto> {

    private DepartmentDto toDtoActual(Department model) {
        DepartmentDto result = new DepartmentDto();
        result.setId(model.getId());
        result.setFullname(model.getFullname());
        result.setShortname(model.getShortname());
        result.setDescription(model.getDescription());
        result.setType(model.getType());
        Department parent = model.getParent();
        if (Objects.nonNull(parent)) {
            result.setParent(toDtoActual(model.getParent()));
        }
        return result;
    }

    @Override
    public Department copyProperties(Department model, DepartmentDto dto) {
        model.setFullname(dto.getFullname());
        model.setShortname(dto.getShortname());
        model.setDescription(dto.getDescription());
        model.setType(dto.getType());
        return model;
    }

    @Override
    public DepartmentDto toDto(Department model) {
        return Optional.ofNullable(model).map(this::toDtoActual).orElse(null);
    }
}
