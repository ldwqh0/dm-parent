package com.dm.uap.dto;

import com.dm.collections.CollectionUtils;
import com.dm.common.dto.IdentifiableDto;
import com.dm.common.validation.constraints.Mobile;
import com.dm.uap.entity.Address;
import com.dm.uap.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableSet;

@JsonInclude(value = Include.NON_ABSENT)
@JsonIgnoreProperties(allowSetters = true, value = {"password"})
public class UserDto implements Serializable, IdentifiableDto<Long> {


    public interface Default {
    }

    public interface New extends Default {
    }

    public interface Update extends Default {
    }

    public interface Patch extends Default {

    }

    public interface ReferenceBy {

    }

    private static final long serialVersionUID = 3115204474399309007L;
    /**
     * 用户Id
     */
    @NotNull(groups = ReferenceBy.class)
    private final Long id;

    /**
     * 工号
     */
    @Size(max = 50, groups = {Default.class})
    private final String no;

    private final String givenName;

    private final String familyName;

    private final String middleName;

    private final String profile;

    private final String website;

    private final User.Gender gender;

    /**
     * 用户名
     */
    @NotNull(groups = {New.class, Update.class})
    @Size(max = 50, groups = {Default.class})
    private final String username;

    /**
     * 用户全称
     */
    @NotNull(groups = {New.class, Update.class})
    @Size(max = 200, groups = {Default.class})
    private final String fullName;

    /**
     * 密码
     */
    @NotNull(groups = {New.class})
    @Size(min = 6, max = 100, groups = {Default.class})
    private final String password;

    /**
     * 用户是否被启用
     */
    @NotNull(groups = {New.class, Update.class})
    private final Boolean enabled = Boolean.FALSE;

    /**
     * 用户email
     */
    @Email(groups = {Default.class})
    @Size(max = 100, groups = {Default.class})
    private final String email;

    private final boolean emailVerified;

    /**
     * 用户手机号
     */
    @Mobile(groups = {Default.class})
    @Size(max = 20, groups = {Default.class})
    private final String mobile;

    private final boolean phoneNumberVerified;

    /**
     * 用户描述信息
     */
    @Size(max = 2000, groups = {Default.class})
    private final String description;

    /**
     * 用户角色
     */
    @Valid
    @NotEmpty(groups = {New.class, Update.class})
    private final Set<UserRoleDto> roles = new HashSet<>();

    /**
     * 景区名称
     */
    @Size(max = 200, groups = {Default.class})
    private final String scenicName;

    /**
     * 区划代码
     */
    @Size(max = 20, groups = {Default.class})
    private final String regionCode;

    /**
     * 用户的职务信息
     */
    @Valid
    private final List<UserPostDto> posts = new ArrayList<>();

    /**
     * 出生日期
     */
    private final LocalDate birthDate;

    /**
     * 用户头像
     */
    private final String profilePhoto;

    private final String zoneinfo;

    private final String local;

    private final Address address;


    public UserDto(@JsonProperty("id") Long id,
                   @JsonProperty("no") String no,
                   @JsonProperty("givenName") String givenName,
                   @JsonProperty("familyName") String familyName,
                   @JsonProperty("middleName") String middleName,
                   @JsonProperty("profile") String profile,
                   @JsonProperty("website") String website,
                   @JsonProperty("gender") User.Gender gender,
                   @JsonProperty("username") String username,
                   @JsonProperty("fullName") String fullName,
                   @JsonProperty("password") String password,
                   @JsonProperty("email") String email,
                   @JsonProperty("emailVerified") Boolean emailVerified,
                   @JsonProperty("mobile") String mobile,
                   @JsonProperty("phoneNumberVerified") Boolean phoneNumberVerified,
                   @JsonProperty("description") String description,
                   @JsonProperty("roles") Set<UserRoleDto> roles,
                   @JsonProperty("scenicName") String scenicName,
                   @JsonProperty("regionCode") String regionCode,
                   @JsonProperty("posts") List<UserPostDto> posts,
                   @JsonProperty("birthDate") LocalDate birthDate,
                   @JsonProperty("profilePhoto") String profilePhoto,
                   @JsonProperty("zoneinfo") String zoneinfo,
                   @JsonProperty("local") String local,
                   @JsonProperty("address") Address address) {
        Address cloneAddress;
        this.id = id;
        this.no = no;
        this.givenName = givenName;
        this.familyName = familyName;
        this.middleName = middleName;
        this.profile = profile;
        this.website = website;
        this.gender = gender;
        this.username = username;
        this.fullName = fullName;
        this.password = password;
        this.email = email;
        this.emailVerified = Boolean.TRUE.equals(emailVerified);
        this.mobile = mobile;
        this.phoneNumberVerified = Boolean.TRUE.equals(phoneNumberVerified);
        this.description = description;
        if (CollectionUtils.isNotEmpty(roles)) {
            this.roles.addAll(roles);
        }
        this.scenicName = scenicName;
        this.regionCode = regionCode;
        if (CollectionUtils.isNotEmpty(posts)) {
            this.posts.addAll(posts);
        }
        this.birthDate = birthDate;
        this.profilePhoto = profilePhoto;
        this.zoneinfo = zoneinfo;
        this.local = local;
        if (Objects.nonNull(address)) {
            this.address = address.clone();
        } else {
            this.address = null;
        }
    }

    public UserDto(String username, String fullName, String password, Set<UserRoleDto> roles) {
        this(null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            username,
            fullName,
            password,
            null,
            null,
            null,
            null,
            null,
            roles,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        );
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getNo() {
        return no;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getProfile() {
        return profile;
    }

    public String getWebsite() {
        return website;
    }

    public User.Gender getGender() {
        return gender;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPassword() {
        return password;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public String getEmail() {
        return email;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public String getMobile() {
        return mobile;
    }

    public boolean isPhoneNumberVerified() {
        return phoneNumberVerified;
    }

    public String getDescription() {
        return description;
    }

    public Set<UserRoleDto> getRoles() {
        return unmodifiableSet(roles);
    }

    public String getScenicName() {
        return scenicName;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public List<UserPostDto> getPosts() {
        return unmodifiableList(posts);
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public String getZoneinfo() {
        return zoneinfo;
    }

    public String getLocal() {
        return local;
    }

    public Address getAddress() {
        return Objects.isNull(address) ? null : address.clone();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return emailVerified == userDto.emailVerified && phoneNumberVerified == userDto.phoneNumberVerified && Objects.equals(id, userDto.id) && Objects.equals(no, userDto.no) && Objects.equals(givenName, userDto.givenName) && Objects.equals(familyName, userDto.familyName) && Objects.equals(middleName, userDto.middleName) && Objects.equals(profile, userDto.profile) && Objects.equals(website, userDto.website) && gender == userDto.gender && Objects.equals(username, userDto.username) && Objects.equals(fullName, userDto.fullName) && Objects.equals(password, userDto.password) && Objects.equals(enabled, userDto.enabled) && Objects.equals(email, userDto.email) && Objects.equals(mobile, userDto.mobile) && Objects.equals(description, userDto.description) && Objects.equals(roles, userDto.roles) && Objects.equals(scenicName, userDto.scenicName) && Objects.equals(regionCode, userDto.regionCode) && Objects.equals(posts, userDto.posts) && Objects.equals(birthDate, userDto.birthDate) && Objects.equals(profilePhoto, userDto.profilePhoto) && Objects.equals(zoneinfo, userDto.zoneinfo) && Objects.equals(local, userDto.local) && Objects.equals(address, userDto.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, no, givenName, familyName, middleName, profile, website, gender, username, fullName, password, enabled, email, emailVerified, mobile, phoneNumberVerified, description, roles, scenicName, regionCode, posts, birthDate, profilePhoto, zoneinfo, local, address);
    }
}

