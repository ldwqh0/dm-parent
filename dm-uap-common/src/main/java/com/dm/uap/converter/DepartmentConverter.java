package com.dm.uap.converter;

import com.dm.uap.dto.DepartmentDto;
import com.dm.uap.entity.Department;

public final class DepartmentConverter {

    private DepartmentConverter() {
    }

    private static DepartmentDto.Builder newBuilder(Department model) {
        DepartmentDto.Builder builder = DepartmentDto.builder()
            .id(model.getId())
            .fullName(model.getFullName())
            .shortname(model.getShortName())
            .description(model.getDescription())
            .type(model.getType())
            .director(model.getDirector())
            .logo(model.getLogo());
        model.getParent().map(DepartmentConverter::toSimpleDto).ifPresent(builder::parent);
        return builder;
    }

    public static DepartmentDto toSimpleDto(Department model) {
        return newBuilder(model).build();
    }

    /**
     * 返回
     */
    public static DepartmentDto toDto(Department model, Long childrenCount, Long userCount) {
        return newBuilder(model)
            .childrenCount(childrenCount)
            .userCount(userCount)
            .build();
    }
}
