package com.dm.uap.converter;

import java.util.Objects;
import org.springframework.stereotype.Component;

import com.dm.common.converter.AbstractConverter;
import com.dm.uap.dto.DepartmentDto;
import com.dm.uap.entity.Department;

@Component
public class DepartmentConverter extends AbstractConverter<Department, DepartmentDto> {

    @Override
    protected DepartmentDto toDtoActual(Department model) {
        DepartmentDto result = new DepartmentDto();
        result.setId(model.getId());
        result.setFullname(model.getFullname());
        result.setShortname(model.getShortname());
        result.setDescription(model.getDescription());
        result.setType(model.getType());
        Department parent = model.getParent();
        if (!Objects.isNull(parent)) {
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
}
