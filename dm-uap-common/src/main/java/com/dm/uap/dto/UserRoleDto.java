package com.dm.uap.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

public class UserRoleDto implements Serializable {
    private static final long serialVersionUID = 899681831260794295L;

    public static final UserRoleDto ROLE_ADMIN = new UserRoleDto(3L, "内置分组", "ROLE_ADMIN");
    public static final UserRoleDto ROLE_AUTHENTICATED = new UserRoleDto(1L, "内置分组", "ROLE_AUTHENTICATED");
    public static final UserRoleDto ROLE_ANONYMOUS = new UserRoleDto(2L, "内置分组", "ROLE_ANONYMOUS");
    /**
     * 角色id
     */
    private final Long id;

    /**
     * 角色组
     */
    private final String group;

    /**
     * 角色名称
     */
    private final String name;

    @JsonCreator
    public UserRoleDto(@JsonProperty("id") Long id,
                       @JsonProperty("group") String group,
                       @JsonProperty("name") String name) {
        this.id = id;
        this.group = group;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getGroup() {
        return group;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRoleDto that = (UserRoleDto) o;
        return Objects.equals(id, that.id) && Objects.equals(group, that.group) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, group, name);
    }

    @Override
    public String toString() {
        return "RoleRequest{" +
            "id=" + id +
            ", group='" + group + '\'' +
            ", name='" + name + '\'' +
            '}';
    }
}
