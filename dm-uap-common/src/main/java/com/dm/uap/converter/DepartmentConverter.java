package com.dm.uap.converter;

import com.dm.uap.dto.DepartmentDto;
import com.dm.uap.entity.Department;

public final class DepartmentConverter {

    private DepartmentConverter() {
    }

    public static DepartmentDto toSimpleDto(Department model) {
        return new DepartmentDto(
            model.getId(),
            model.getFullName(),
            model.getShortName(),
            model.getDescription(),
            model.getType(),
            model.getParent().map(DepartmentConverter::toSimpleDto).orElse(null),
            model.getDirector(),
            model.getLogo(),
            null, null
        );
    }


    /**
     * 返回
     */
    public static DepartmentDto toDto(Department model, Long childrenCount, Long userCount) {
        return new DepartmentDto(
            model.getId(),
            model.getFullName(),
            model.getShortName(),
            model.getDescription(),
            model.getType(),
            model.getParent().map(DepartmentConverter::toSimpleDto).orElse(null),
            model.getDirector(),
            model.getLogo(),
            childrenCount,
            userCount
        );
    }
}
