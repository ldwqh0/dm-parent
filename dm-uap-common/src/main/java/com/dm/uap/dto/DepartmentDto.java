package com.dm.uap.dto;

import com.dm.common.dto.IdentifiableDto;
import com.dm.uap.entity.Department.Types;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    /**
     * 部门ID
     */
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
     * 部门类型 ，分别是 ORGANS=机构,DEPARTMENT=部门,GROUP=分组
     */
    @NotNull(groups = {New.class})
    private Types type;

    public DepartmentDto() {
        super();
    }

    /**
     * 是否有子部门
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean hasChildren = false;

    /**
     * 子部门个数
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long childrenCount = 0;

    /**
     * 用户数量
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long userCount=0;

    public DepartmentDto(Long id) {
        super();
        this.id = id;
    }

    /**
     * 上级部门信息
     */
    @JsonIgnoreProperties({"parent", "description", "parents"})
    private DepartmentDto parent;

    public Optional<DepartmentDto> getParent() {
        return Optional.ofNullable(parent);
    }

    /**
     * 负责人
     */
    private UserDto director;


    public Optional<UserDto> getDirector() {
        return Optional.ofNullable(director);
    }

    /**
     * logo, 部门的logo字符串表现方式，可能是文件的ID，URL路径，或者BASE64形式的字符串，具体由前端控制
     */
    private String logo;

}
