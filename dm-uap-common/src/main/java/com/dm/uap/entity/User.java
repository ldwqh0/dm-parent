package com.dm.uap.entity;

import com.dm.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "dm_user_", uniqueConstraints = {
    @UniqueConstraint(name = "UK_dm_user_username_", columnNames = {"username_"}),
    @UniqueConstraint(name = "UK_dm_user_email_", columnNames = {"email_"}),
    @UniqueConstraint(name = "UK_dm_user_mobile_", columnNames = {"mobile_"}),
    @UniqueConstraint(name = "UK_dm_user_no_", columnNames = "no_")
})
public class User extends AbstractEntity {

    /**
     * 用户名
     */
    @Column(name = "username_", unique = true, length = 50)
    @NotNull
    private String username;

    /**
     * 用户工号
     */
    @Column(name = "no_", length = 50)
    private String no;

    @Column(name = "given_name_", length = 50)
    private String givenName;

    @Column(name = "family_name_", length = 50)
    private String familyName;

    @Column(name = "middle_name_", length = 50)
    private String middleName;

    @Column(name = "profile_", length = 1000)
    private String profile;

    @Column(name = "website_", length = 1000)
    private String website;

    @Column(name = "gender_", length = 8)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    /**
     * 用户密码
     */
    @Column(name = "password_", length = 100)
    private String password;

    @Column(name = "expired_", nullable = false)
    private boolean accountExpired = false;

    @Column(name = "credentials_expired_", nullable = false)
    private boolean credentialsExpired = false;

    @Column(name = "enabled_", nullable = false)
    private boolean enabled = true;

    @Column(name = "fullname_", length = 200)
    private String fullname;

    @Column(name = "email_", length = 100, unique = true)
    private String email;

    @Column(name = "email_verified_", nullable = false)
    private boolean emailVerified = false;

    @Column(name = "mobile_", length = 20, unique = true)
    private String mobile;

    @Column(name = "phone_number_verified_", nullable = false)
    private boolean phoneNumberVerified = false;

    @Column(name = "description_", length = 2000)
    private String description;

    @Column(name = "zoneinfo_", length = 100)
    private String zoneinfo;

    @Column(name = "local_", length = 100)
    private String local;

    @Embedded
    private Address address;

    /**
     * 用户的角色信息，在UAP系统中，仅仅保存用户的角色信息。<br>
     * 在用户系统中,不对角色信息进行单独维护，仅仅作为用户的附加信息而存在。
     */
    @ManyToMany
    @JoinTable(name = "dm_user_role_", joinColumns = {
        @JoinColumn(name = "user_id_", foreignKey = @ForeignKey(name = "FK_dm_user_role_user_"))
    }, inverseJoinColumns = {
        @JoinColumn(name = "role_id_", foreignKey = @ForeignKey(name = "FK_dm_user_role_role_"))
    }, uniqueConstraints = {
        @UniqueConstraint(name = "UK_dm_user_role_user_role_", columnNames = {"user_id_", "role_id_"})
    })
    private Set<UserRole> roles = new HashSet<>();

    public void setRoles(Set<UserRole> roles) {
        this.roles.clear();
        this.roles.addAll(roles);
    }

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
    private Map<Department, String> posts = new HashMap<>();

    public void setPosts(Map<Department, String> posts) {
        this.posts.clear();
        this.posts.putAll(posts);
    }

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
    private Map<Department, Long> orders = new HashMap<>();

    public void setOrders(Map<Department, Long> orders) {
        this.orders.clear();
        this.orders.putAll(orders);
    }

    @Column(name = "region_code_", length = 20)
    private String regionCode;

    @ElementCollection
    @JoinTable(name = "dm_user_attribute_",
        joinColumns = {
            @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_dm_user_attribute_user_id_"))
        })
    @MapKeyColumn(name = "key")
    @Column(name = "value")
    private Map<String, String> attributes = new HashMap<>();

    public void setAttributes(Map<String, String> attributes) {
        this.attributes.clear();
        this.attributes.putAll(attributes);
    }

    @Column(name = "birth_date_")
    private LocalDate birthDate;

    @Column(name = "scenic_name_", length = 200)
    private String scenicName;

    @Column(name = "profile_photo_", length = 4000)
    private String profilePhoto;

    @LastModifiedDate
    @Column(name = "last_modified_time_")
    private ZonedDateTime lastModifiedTime;

    public enum Gender {
        /**
         * 男性
         */
        male,
        /**
         * 女性
         */
        female
    }
}
