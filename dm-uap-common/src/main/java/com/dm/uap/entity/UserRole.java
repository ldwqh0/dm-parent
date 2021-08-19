package com.dm.uap.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户的角色，这个不同于角色，它仅仅表示用户的角色，不能用于角色的权限控制
 */
@Entity
@Getter
@Setter
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

}
