package com.dm.uap.converter;

import com.dm.common.converter.Converter;
import com.dm.uap.dto.DepartmentDto;
import com.dm.uap.entity.Department;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class DepartmentConverter implements Converter<Department, DepartmentDto> {

    public DepartmentDto toSimpleWithoutParent(Department model) {
        DepartmentDto result = new DepartmentDto();
        result.setId(model.getId());
        result.setFullname(model.getFullname());
        result.setShortname(model.getShortname());
        result.setDescription(model.getDescription());
        result.setType(model.getType());
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
        return Optional.ofNullable(model).map(m -> {
            DepartmentDto result = toSimpleWithoutParent(model);
            Department parent = model.getParent();
            if (Objects.nonNull(parent)) {
                DepartmentDto parentDto = toSimpleWithoutParent(model.getParent());
                parentDto.setHasChildren(true);
                result.setParent(parentDto);
            }
            return result;
        }).orElse(null);
    }
}
