package com.dm.uap.converter;

import com.dm.uap.dto.DepartmentDto;
import com.dm.uap.entity.Department;

public final class DepartmentConverter {

    private DepartmentConverter() {
    }

    public static DepartmentDto toSimpleDto(Department model) {
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
     */
    public static DepartmentDto toDto(Department model) {
        DepartmentDto result = toSimpleDto(model);
        model.getParent().map(DepartmentConverter::toSimpleDto).ifPresent(result::setParent);
        return result;
    }
}
