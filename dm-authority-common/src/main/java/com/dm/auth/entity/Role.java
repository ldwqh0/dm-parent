package com.dm.auth.entity;

import com.dm.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "dm_role_", uniqueConstraints = {@UniqueConstraint(name = "UK_dm_role_group_name_", columnNames = {"group_", "name_"})})
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

    @Column(name = "group_", nullable = false)
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
    private Set<Menu> menus;

    public String getFullName() {
        return group + "_" + name;
    }
}
