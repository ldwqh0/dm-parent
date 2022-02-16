package com.dm.auth.dto;

import com.dm.auth.entity.Role.Status;
import com.dm.common.dto.IdentifiableDto;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

/**
 * 角色数据结构
 *
 * @author LiDong
 */
@JsonInclude(NON_EMPTY)
public class RoleDto implements IdentifiableDto<Long>, Serializable {
    private static final long serialVersionUID = 4725729366179649819L;

    @JsonCreator
    public RoleDto(@JsonProperty("id") Long id,
                   @JsonProperty("name") String name,
                   @JsonProperty("group") String group,
                   @JsonProperty("description") String description,
                   @JsonProperty("state") Status state) {
        this.id = id;
        this.name = name;
        this.group = group;
        this.description = description;
        this.state = state;
    }

    public interface ReferenceBy {
    }

    public interface New extends Default {
    }

    public interface Update extends Default {
    }

    public interface Default {

    }

    /**
     * 角色ID
     */
    @NotNull(groups = ReferenceBy.class)
    private final Long id;

    /**
     * 角色名称
     *
     * @apiNote 任何时候，角色名称不能为空
     */
    @NotBlank(groups = {New.class, Update.class})
    @Size(max = 100, groups = {Default.class})
    private final String name;

    /**
     * 角色描述
     */
    @Size(max = 2000, groups = {Default.class})
    private final String description;

    /**
     * 角色状态
     */
    @NotNull(groups = {New.class, Update.class})
    private final Status state;

    /**
     * 角色所属组
     */
    @Valid
    @NotNull(groups = {New.class, Update.class})
    private final String group;

    /**
     * 角色全称
     *
     * @return 获取角色的全程
     * @apiNote 角色的全称是 {group}_{name}
     */
    public String getFullName() {
        return group + "_" + name;
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Status getState() {
        return state;
    }

    public String getGroup() {
        return group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleDto roleDto = (RoleDto) o;
        return Objects.equals(id, roleDto.id) && Objects.equals(name, roleDto.name) && Objects.equals(description, roleDto.description) && state == roleDto.state && Objects.equals(group, roleDto.group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, state, group);
    }

    @Override
    public String toString() {
        return "RoleDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", state=" + state +
                ", group='" + group + '\'' +
                '}';
    }
}
