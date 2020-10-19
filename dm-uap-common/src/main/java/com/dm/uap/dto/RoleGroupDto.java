package com.dm.uap.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

import javax.validation.constraints.Min;
import java.io.Serializable;

@Data
@JsonInclude(value = Include.NON_EMPTY)
public class RoleGroupDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 任何时候，不能修改或者编辑内置角色组
     */
    @Min(value = 2)
    private Long id;
    private String name;
    private String description;

    public RoleGroupDto() {
    }

    public RoleGroupDto(String name) {
        this.name = name;
    }

    public RoleGroupDto(Long id, String name, String description) {
        super();
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public RoleGroupDto(Long id) {
        super();
        this.id = id;
    }
}
