package com.dm.uap.dto;

import com.dm.common.dto.IdentifiableDto;
import com.dm.uap.entity.Department.Types;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Optional;

@Data
@JsonInclude(value = Include.NON_EMPTY)
public class DepartmentDto implements IdentifiableDto<Long>, Serializable {

    public interface New {

    }

    public interface ReferenceBy {

    }

    private static final long serialVersionUID = -4966481409754529111L;

    @NotNull(groups = ReferenceBy.class)
    private Long id;

    /**
     * 完整名称
     */
    @NotNull(groups = {New.class})
    private String fullname;

    /**
     * 短名称
     */
    @NotNull(groups = {New.class})
    private String shortname;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 部门类型 ，分别是 ORGANS=机构/,DEPARTMENT=部门/,GROUP=分组/
     */
    @NotNull(groups = {New.class})
    private Types type;

    public DepartmentDto() {
        super();
    }

    private boolean hasChildren = false;

    private long childrenCount = 0;

    public DepartmentDto(Long id) {
        super();
        this.id = id;
    }

    @JsonIgnoreProperties({"parent", "description", "parents"})
    private DepartmentDto parent;

    public Optional<DepartmentDto> getParent() {
        return Optional.ofNullable(parent);
    }

    private UserDto director;

    public Optional<UserDto> getDirector() {
        return Optional.ofNullable(director);
    }

    private String logo;

}
