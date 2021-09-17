package com.dm.uap.dto;

import com.dm.common.dto.IdentifiableDto;
import com.dm.uap.entity.Department.Types;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

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
    private long userCount = 0;

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
    private String director;


    public String getDirector() {
        return this.director;
    }

    /**
     * logo, 部门的logo字符串表现方式，可能是文件的ID，URL路径，或者BASE64形式的字符串，具体由前端控制
     */
    private String logo;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Types getType() {
        return type;
    }

    public void setType(Types type) {
        this.type = type;
    }

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public long getChildrenCount() {
        return childrenCount;
    }

    public void setChildrenCount(long childrenCount) {
        this.childrenCount = childrenCount;
    }

    public long getUserCount() {
        return userCount;
    }

    public void setUserCount(long userCount) {
        this.userCount = userCount;
    }

    public void setParent(DepartmentDto parent) {
        this.parent = parent;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DepartmentDto that = (DepartmentDto) o;
        return hasChildren == that.hasChildren && childrenCount == that.childrenCount && userCount == that.userCount && Objects.equals(id, that.id) && Objects.equals(fullname, that.fullname) && Objects.equals(shortname, that.shortname) && Objects.equals(description, that.description) && type == that.type && Objects.equals(parent, that.parent) && Objects.equals(director, that.director) && Objects.equals(logo, that.logo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullname, shortname, description, type, hasChildren, childrenCount, userCount, parent, director, logo);
    }
}
