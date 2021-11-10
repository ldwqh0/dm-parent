package com.dm.auth.entity;

import com.dm.common.entity.AbstractEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "dm_role_", uniqueConstraints = {
    @UniqueConstraint(name = "UK_dm_role_group_name_", columnNames = {"group_", "name_"})
}, indexes = {
    @Index(name = "IDX_dm_role_state_", columnList = "state_")
})
public class Role extends AbstractEntity {


    public enum Status {
        /**
         * 标识角色已启用
         */
        ENABLED,
        /**
         * 标识角色被禁用
         */
        DISABLED
    }

    @Column(name = "name_", length = 100, nullable = false)
    private String name;

    @Column(name = "group_", length = 100, nullable = false)
    private String group;

    @Column(name = "state_", length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private Status state;

    @Column(name = "description_", length = 2000)
    private String description;

    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(name = "dm_role_menu_", joinColumns = {
        @JoinColumn(name = "role_id_", referencedColumnName = "id_", foreignKey = @ForeignKey(name = "FK_dm_role_menu_role_id_"))
    }, inverseJoinColumns = {
        @JoinColumn(name = "menu_id_", referencedColumnName = "id_", foreignKey = @ForeignKey(name = "FK_dm_role_menu_menu_id_"))
    }, indexes = {
        @Index(columnList = "role_id_", name = "IDX_dm_role_menu_role_id_"),
        @Index(columnList = "menu_id_", name = "IDX_dm_role_menu_menu_id_")
    })
    private final Set<Menu> menus = new HashSet<>();

    public Role() {
    }

    public Role(String name, String group, Status state, String description, Set<Menu> menus) {
        this.name = name;
        this.group = group;
        this.state = state;
        this.description = description;
        this.setMenus(menus);
    }

    public String getFullName() {
        return group + "_" + name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Status getState() {
        return state;
    }

    public void setState(Status state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Menu> getMenus() {
        return menus;
    }

    public void setMenus(Set<Menu> menus) {
        this.menus.clear();
        this.menus.addAll(menus);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Role role = (Role) o;
        return Objects.equals(name, role.name) && Objects.equals(group, role.group) && state == role.state && Objects.equals(description, role.description) && Objects.equals(menus, role.menus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, group, state, description, menus);
    }

    @Override
    public String toString() {
        return "Role{" +
            "name='" + name + '\'' +
            ", group='" + group + '\'' +
            ", state=" + state +
            ", description='" + description + '\'' +
            ", menus=" + menus +
            '}';
    }
}
