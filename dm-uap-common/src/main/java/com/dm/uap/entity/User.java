package com.dm.uap.entity;

import com.dm.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "dm_user_", uniqueConstraints = {
    @UniqueConstraint(name = "UK_dm_user_username_", columnNames = {"username_"}),
    @UniqueConstraint(name = "UK_dm_user_email_", columnNames = {"email_"}),
    @UniqueConstraint(name = "UK_dm_user_mobile_", columnNames = {"mobile_"})
})
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

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "dm_user_role_", joinColumns = {
        @JoinColumn(name = "user_id_", foreignKey = @ForeignKey(name = "FK_dm_user_role_user_"))
    }, uniqueConstraints = {
        @UniqueConstraint(name = "UK_dm_user_role_user_role_", columnNames = {"user_id_", "role_id_"})
    })
    private List<Role> roles;

    /**
     * 用户的职务信息
     */
    @ElementCollection
    @JoinTable(name = "dm_user_post_", joinColumns = {
        @JoinColumn(name = "user_id_", foreignKey = @ForeignKey(name = "FK_dm_user_post_user_id_"))
    }, uniqueConstraints = {
        @UniqueConstraint(name = "UK_user_id_department_id_post_", columnNames = {"user_id_", "department_id_", "post_"})
    }, indexes = {
        @Index(name = "IDX_dm_user_post_user_id_", columnList = "user_id_"),
        @Index(name = "IDX_dm_user_post_department_id_", columnList = "department_id_")
    })
    @MapKeyJoinColumn(name = "department_id_", foreignKey = @ForeignKey(name = "FK_dm_user_post_department_id_"))
    @Column(name = "post_", length = 50)
    private Map<Department, String> posts;

    /**
     * 用户的排序
     */
    @ElementCollection
    @JoinTable(name = "dm_user_order_", joinColumns = {
        @JoinColumn(name = "user_id_", foreignKey = @ForeignKey(name = "FK_dm_user_order_user_id_"))
    }, indexes = {
        @Index(name = "IDX_department_id_user_id_", columnList = "user_id_"),
        @Index(name = "IDX_department_id_department_id_", columnList = "department_id_")
    })
    @MapKeyJoinColumn(name = "department_id_", foreignKey = @ForeignKey(name = "FK_dm_user_order_department_id_"))
    @Column(name = "order_")
    private Map<Department, Long> orders;

    @Column(name = "region_code_", length = 20)
    private String regionCode;

    @Column(name = "scenic_name_", length = 200)
    private String scenicName;
}
