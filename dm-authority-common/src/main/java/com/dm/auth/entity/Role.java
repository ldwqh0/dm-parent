package com.dm.auth.entity;

import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.dm.common.entity.AbstractEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "dm_role_", uniqueConstraints = {@UniqueConstraint(columnNames = {"group_", "name_"})})
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
        @JoinColumn(name = "role_id_", referencedColumnName = "id_")
    }, inverseJoinColumns = {
        @JoinColumn(name = "menu_id_", referencedColumnName = "id_")
    }, indexes = {
        @Index(columnList = "role_id_", name = "IDX_dm_role_menu_role_id_")
    })
    private Set<Menu> menus;

    @ElementCollection(fetch = FetchType.LAZY)
    @JoinTable(name = "dm_role_resource_operation_", joinColumns = {
        @JoinColumn(name = "role_id_", referencedColumnName = "id_")})
    @OrderColumn(name = "order_by_")
    @MapKeyJoinColumn(name = "resource_id_")
    private Map<AuthResource, ResourceOperation> resourceOperations;

    public String getFullName() {
        return group + "_" + name;
    }
}
