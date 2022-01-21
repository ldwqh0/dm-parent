package com.dm.uap.entity;

import com.dm.data.domain.AbstractEntity;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;

import static java.util.Collections.unmodifiableMap;
import static java.util.Collections.unmodifiableSet;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "dm_user_", indexes = {
    @Index(name = "UDX_dm_user_username_", columnList = "username_", unique = true),
    @Index(name = "UDX_dm_user_email_", columnList = "email_", unique = true),
    @Index(name = "UDX_dm_user_mobile_", columnList = "mobile_", unique = true),
    @Index(name = "UDX_dm_user_no_", columnList = "no_", unique = true),
    @Index(name = "IDX_dm_user_enabled_", columnList = "enabled_")
})
public class User extends AbstractEntity {

    /**
     * 用户名
     */
    @Column(name = "username_", length = 50)
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

    @Column(name = "full_name_", length = 200)
    private String fullName;

    @Column(name = "email_", length = 100)
    private String email;

    @Column(name = "email_verified_", nullable = false)
    private boolean emailVerified = false;

    @Column(name = "mobile_", length = 20)
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
            @JoinColumn(name = "user_id_", foreignKey = @ForeignKey(name = "FK_dm_user_attribute_user_id_"))
        })
    @MapKeyColumn(name = "key_")
    @Column(name = "value_")
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


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAccountExpired() {
        return accountExpired;
    }

    public void setAccountExpired(boolean accountExpired) {
        this.accountExpired = accountExpired;
    }

    public boolean isCredentialsExpired() {
        return credentialsExpired;
    }

    public void setCredentialsExpired(boolean credentialsExpired) {
        this.credentialsExpired = credentialsExpired;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public boolean isPhoneNumberVerified() {
        return phoneNumberVerified;
    }

    public void setPhoneNumberVerified(boolean phoneNumberVerified) {
        this.phoneNumberVerified = phoneNumberVerified;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getZoneinfo() {
        return zoneinfo;
    }

    public void setZoneinfo(String zoneinfo) {
        this.zoneinfo = zoneinfo;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public Address getAddress() {
        return ObjectUtils.clone(address);
    }

    public void setAddress(Address address) {
        this.address = ObjectUtils.clone(address);
    }

    public Set<UserRole> getRoles() {
        return unmodifiableSet(roles);
    }

    public Map<Department, String> getPosts() {
        return unmodifiableMap(posts);
    }

    public Map<Department, Long> getOrders() {
        return unmodifiableMap(orders);
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public Map<String, String> getAttributes() {
        return unmodifiableMap(attributes);
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getScenicName() {
        return scenicName;
    }

    public void setScenicName(String scenicName) {
        this.scenicName = scenicName;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public ZonedDateTime getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(ZonedDateTime lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return accountExpired == user.accountExpired && credentialsExpired == user.credentialsExpired && enabled == user.enabled && emailVerified == user.emailVerified && phoneNumberVerified == user.phoneNumberVerified && Objects.equals(username, user.username) && Objects.equals(no, user.no) && Objects.equals(givenName, user.givenName) && Objects.equals(familyName, user.familyName) && Objects.equals(middleName, user.middleName) && Objects.equals(profile, user.profile) && Objects.equals(website, user.website) && gender == user.gender && Objects.equals(password, user.password) && Objects.equals(fullName, user.fullName) && Objects.equals(email, user.email) && Objects.equals(mobile, user.mobile) && Objects.equals(description, user.description) && Objects.equals(zoneinfo, user.zoneinfo) && Objects.equals(local, user.local) && Objects.equals(address, user.address) && Objects.equals(roles, user.roles) && Objects.equals(posts, user.posts) && Objects.equals(orders, user.orders) && Objects.equals(regionCode, user.regionCode) && Objects.equals(attributes, user.attributes) && Objects.equals(birthDate, user.birthDate) && Objects.equals(scenicName, user.scenicName) && Objects.equals(profilePhoto, user.profilePhoto) && Objects.equals(lastModifiedTime, user.lastModifiedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), username, no, givenName, familyName, middleName, profile, website, gender, password, accountExpired, credentialsExpired, enabled, fullName, email, emailVerified, mobile, phoneNumberVerified, description, zoneinfo, local, address, roles, posts, orders, regionCode, attributes, birthDate, scenicName, profilePhoto, lastModifiedTime);
    }
}
