package com.dm.uap.dto;

import com.dm.data.domain.Identifiable;
import com.dm.uap.entity.Department.Types;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@JsonInclude(NON_EMPTY)
public class DepartmentDto implements Identifiable<Long>, Serializable {
    private static final long serialVersionUID = -4966481409754529111L;

    public interface New {

    }

    public interface ReferenceBy {

    }

    /**
     * 部门ID
     */
    @NotNull(groups = ReferenceBy.class)
    private final Long id;

    /**
     * 完整名称
     */
    @NotNull(groups = {New.class})
    private final String fullName;

    /**
     * 短名称
     */
    @NotNull(groups = {New.class})
    private final String shortname;

    /**
     * 描述信息
     */
    private final String description;

    /**
     * 部门类型 ，分别是 ORGANS=机构,DEPARTMENT=部门,GROUP=分组
     */
    @NotNull(groups = {New.class})
    private final Types type;

    /**
     * 子部门个数
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final Long childrenCount;

    /**
     * 用户数量
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final Long userCount;

    /**
     * 上级部门信息
     */
    @JsonIgnoreProperties({"parent", "description", "parents"})
    private final DepartmentDto parent;

    public Optional<DepartmentDto> getParent() {
        return Optional.ofNullable(parent);
    }

    /**
     * 负责人
     */
    private final String director;

    public String getDirector() {
        return this.director;
    }

    /**
     * logo, 部门的logo字符串表现方式，可能是文件的ID，URL路径，或者BASE64形式的字符串，具体由前端控制
     */
    private final String logo;

    @JsonCreator
    private DepartmentDto(@JsonProperty("id") Long id,
                          @JsonProperty("fullName") String fullName,
                          @JsonProperty("shortname") String shortname,
                          @JsonProperty("description") String description,
                          @JsonProperty("type") Types type,
                          @JsonProperty("parent") DepartmentDto parent,
                          @JsonProperty("director") String director,
                          @JsonProperty("logo") String logo) {
        this(id, fullName, shortname, description, type, parent, director, logo, null, null);
    }

    private DepartmentDto(Long id,
                          String fullName,
                          String shortname,
                          String description,
                          Types type,
                          DepartmentDto parent,
                          String director,
                          String logo,
                          Long childrenCount,
                          Long userCount) {
        this.id = id;
        this.fullName = fullName;
        this.shortname = shortname;
        this.description = description;
        this.type = type;
        this.childrenCount = childrenCount;
        this.userCount = userCount;
        this.parent = parent;
        this.director = director;
        this.logo = logo;
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getShortname() {
        return shortname;
    }

    public String getDescription() {
        return description;
    }

    public Types getType() {
        return type;
    }

    /**
     * 是否有子部门
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public boolean isHasChildren() {
        return this.childrenCount > 0;
    }

    public long getChildrenCount() {
        return childrenCount;
    }

    public long getUserCount() {
        return userCount;
    }

    public String getLogo() {
        return logo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DepartmentDto that = (DepartmentDto) o;
        return childrenCount == that.childrenCount && userCount == that.userCount && Objects.equals(id, that.id) && Objects.equals(fullName, that.fullName) && Objects.equals(shortname, that.shortname) && Objects.equals(description, that.description) && type == that.type && Objects.equals(parent, that.parent) && Objects.equals(director, that.director) && Objects.equals(logo, that.logo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullName, shortname, description, type, childrenCount, userCount, parent, director, logo);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Long id;
        private String fullName;
        private String shortname;
        private String description;
        private Types type;
        private Long childrenCount;
        private Long userCount;
        private DepartmentDto parent;
        private String director;
        private String logo;

        private Builder() {
        }

        public static Builder aDepartmentDto() {
            return new Builder();
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public Builder shortname(String shortname) {
            this.shortname = shortname;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder type(Types type) {
            this.type = type;
            return this;
        }

        public Builder childrenCount(Long childrenCount) {
            this.childrenCount = childrenCount;
            return this;
        }

        public Builder userCount(Long userCount) {
            this.userCount = userCount;
            return this;
        }

        public Builder parent(DepartmentDto parent) {
            this.parent = parent;
            return this;
        }

        public Builder director(String director) {
            this.director = director;
            return this;
        }

        public Builder logo(String logo) {
            this.logo = logo;
            return this;
        }

        public DepartmentDto build() {
            return new DepartmentDto(id, fullName, shortname, description, type, parent, director, logo, childrenCount, userCount);
        }
    }
}
