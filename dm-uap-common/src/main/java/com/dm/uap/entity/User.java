package com.dm.uap.entity;

import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.dm.auth.entity.Role;
import com.dm.common.entity.AbstractEntity;

import javax.persistence.JoinColumn;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "dm_user_")
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends AbstractEntity {

    @Column(name = "username_", unique = true, length = 50)
    @NotNull
    private String username;

    @Column(name = "password_", length = 100)
    private String password;

    @Column(name = "expired_", nullable = false)
    private boolean accountExpired = false;

    @Column(name = "credentials_expired_", nullable = false)
    private boolean credentialsExpired = false;

    @Column(name = "enabled_", nullable = false)
    private boolean enabled = true;

    @Column(name = "locked_", nullable = false)
    private boolean locked = false;

    @Column(name = "fullname_", length = 200)
    private String fullname;

    @Column(name = "email_", length = 100, unique = true)
    private String email;

    @Column(name = "mobile_", length = 20, unique = true)
    private String mobile;

    @Column(name = "description_", length = 2000)
    private String description;

    @Column(name = "order_")
    private Long order;

    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(name = "dm_user_role_", joinColumns = {
            @JoinColumn(name = "user_", referencedColumnName = "id_")
    }, inverseJoinColumns = {
            @JoinColumn(name = "role_", referencedColumnName = "id_")
    }, indexes = {
            @Index(columnList = "user_", name = "IDX_dm_user_role_user_")
    }, uniqueConstraints = {
            @UniqueConstraint(columnNames = { "user_", "role_" })
    })
    private List<Role> roles;

    /**
     * 用户的职务信息
     */
    @ElementCollection
    @JoinTable(name = "dm_user_post_", joinColumns = {
            @JoinColumn(name = "user_id_")
    }, uniqueConstraints = {
            @UniqueConstraint(columnNames = { "user_id_", "department_id_", "post_" })
    })
    @MapKeyJoinColumn(name = "department_id_")
    @Column(name = "post_", length = 50)
    private Map<Department, String> posts;

    /**
     * 用户的排序
     */
    @ElementCollection
    @JoinTable(name = "dm_user_order_", joinColumns = {
            @JoinColumn(name = "user_id_")
    })
    @MapKeyJoinColumn(name = "department_id_")
    @Column(name = "order_")
    private Map<Department, Long> orders;

    @Column(name = "region_code_", length = 20)
    private String regionCode;

    @Column(name = "scenic_name_", length = 200)
    private String scenicName;
}
