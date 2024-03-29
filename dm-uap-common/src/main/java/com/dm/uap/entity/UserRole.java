package com.dm.uap.entity;

import javax.persistence.*;

/**
 * 用户的角色，这个不同于角色，它仅仅表示用户的角色，不能用于角色的权限控制
 */
@Entity
@Table(name = "dm_role_", uniqueConstraints = {
    @UniqueConstraint(name = "UK_dm_role_group_name_", columnNames = {"group_", "name_"})
}, indexes = {
    @Index(name = "IDX_dm_role_state_", columnList = "state_")
})
public class UserRole {

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

    /**
     * 角色id
     */
    @Id
    @Column(name = "id_", nullable = false)
    private Long id;

    /**
     * 角色组
     */
    @Column(name = "group_", length = 100, nullable = false)
    private String group;

    /**
     * 角色名称
     */
    @Column(name = "name_", length = 100, nullable = false)
    private String name;

    @Version
    @Column(name = "version_", nullable = false)
    private Long version;

    @Column(name = "state_", length = 50, nullable = false)
    private String state = "ENABLED";

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public UserRole() {
    }

    public UserRole(Long id, String group, String name) {
        this.id = id;
        this.group = group;
        this.name = name;
    }
}
