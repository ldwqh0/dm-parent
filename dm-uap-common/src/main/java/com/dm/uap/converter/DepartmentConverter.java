package com.dm.uap.converter;

import com.dm.common.converter.Converter;
import com.dm.uap.dto.DepartmentDto;
import com.dm.uap.entity.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class DepartmentConverter implements Converter<Department, DepartmentDto> {

    private UserConverter userConverter;

    @Autowired
    @Lazy
    public void setUserConverter(UserConverter userConverter) {
        this.userConverter = userConverter;
    }

    public DepartmentDto toSimpleWithoutParent(Department model) {
        DepartmentDto result = new DepartmentDto();
        result.setId(model.getId());
        result.setFullname(model.getFullname());
        result.setShortname(model.getShortname());
        result.setDescription(model.getDescription());
        result.setType(model.getType());
        result.setLogo(model.getLogo());
        model.getDirector().map(userConverter::toSimpleDto).ifPresent(result::setDirector);
        return result;
    }

    @Override
    public Department copyProperties(Department model, DepartmentDto dto) {
        model.setFullname(dto.getFullname());
        model.setShortname(dto.getShortname());
        model.setDescription(dto.getDescription());
        model.setType(dto.getType());
        model.setLogo(dto.getLogo());
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
