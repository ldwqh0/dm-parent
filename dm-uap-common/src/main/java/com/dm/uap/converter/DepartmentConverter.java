package com.dm.uap.converter;

import com.dm.uap.dto.DepartmentDto;
import com.dm.uap.entity.Department;

import java.util.Optional;

public final class DepartmentConverter {

    private DepartmentConverter() {
    }

    public static DepartmentDto toSimpleWithoutParent(Department model) {
        DepartmentDto result = new DepartmentDto();
        result.setId(model.getId());
        result.setFullname(model.getFullname());
        result.setShortname(model.getShortname());
        result.setDescription(model.getDescription());
        result.setType(model.getType());
        result.setLogo(model.getLogo());
        result.setDirector(model.getDirector());
        return result;
    }

    /**
     * 返回
     *
     * @param model
     */
    public static DepartmentDto toDto(Department model) {
        return Optional.ofNullable(model).map(m -> {
            DepartmentDto result = toSimpleWithoutParent(model);
            Optional.ofNullable(model.getParent())
                .map(DepartmentConverter::toSimpleWithoutParent)
                .ifPresent(parent -> {
                    parent.setHasChildren(true);
                    result.setParent(parent);
                });
            return result;
        }).orElse(null);
    }
}
