package com.dm.uap.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

/**
 * 用户的角色，这个不同于角色，它仅仅表示用户的角色，不能用于角色的权限控制
 */
@Entity
@Table(name = "dm_role_")
public class UserRole {

    /**
     * 角色id
     */
    @Id
    @Column(name = "id_", nullable = false)
    private Long id;

    /**
     * 角色组
     */
    @Column(name = "group_", length = 100)
    private String group;

    /**
     * 角色名称
     */
    @Column(name = "name_", length = 100)
    private String name;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRole userRole = (UserRole) o;
        return Objects.equals(id, userRole.id) && Objects.equals(group, userRole.group) && Objects.equals(name, userRole.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, group, name);
    }
}
