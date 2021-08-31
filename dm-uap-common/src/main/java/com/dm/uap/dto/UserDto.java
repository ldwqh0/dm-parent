package com.dm.uap.dto;

import com.dm.common.dto.IdentifiableDto;
import com.dm.common.validation.constraints.Mobile;
import com.dm.uap.entity.Address;
import com.dm.uap.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
    private Long id;

    /**
     * 工号
     */
    @Size(max = 50, groups = {Default.class})
    private String no;

    private String givenName;

    private String familyName;

    private String middleName;

    private String profile;

    private String website;

    private User.Gender gender;

    /**
     * 用户名
     */
    @NotNull(groups = {New.class, Update.class})
    @Size(max = 50, groups = {Default.class})
    private String username;

    /**
     * 用户全称
     */
    @NotNull(groups = {New.class, Update.class})
    @Size(max = 200, groups = {Default.class})
    private String fullname;

    /**
     * 密码
     */
    @NotNull(groups = {New.class})
    @Size(min = 6, max = 100, groups = {Default.class})
    private String password;

    /**
     * 用户是否被启用
     */
    @NotNull(groups = {New.class, Update.class})
    private Boolean enabled = Boolean.FALSE;

    /**
     * 用户email
     */
    @Email(groups = {Default.class})
    @Size(max = 100, groups = {Default.class})
    private String email;

    private boolean emailVerified = false;

    /**
     * 用户手机号
     */
    @Mobile(groups = {Default.class})
    @Size(max = 20, groups = {Default.class})
    private String mobile;

    private boolean phoneNumberVerified = false;

    /**
     * 用户描述信息
     */
    @Size(max = 2000, groups = {Default.class})
    private String description;

    /**
     * 用户角色
     */
    @Valid
    @NotEmpty(groups = {New.class, Update.class})
    private Set<UserRoleDto> roles;

    /**
     * 景区名称
     */
    @Size(max = 200, groups = {Default.class})
    private String scenicName;

    /**
     * 区划代码
     */
    @Size(max = 20, groups = {Default.class})
    private String regionCode;

    /**
     * 用户的职务信息
     */
    @Valid
    private List<UserPostDto> posts;

    /**
     * 出生日期
     */
    private LocalDate birthDate;

    /**
     * 用户头像
     */
    private String profilePhoto;

    private String zoneinfo;

    private String local;

    private Address address;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public User.Gender getGender() {
        return gender;
    }

    public void setGender(User.Gender gender) {
        this.gender = gender;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
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

    public Set<UserRoleDto> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRoleDto> roles) {
        this.roles = roles;
    }

    public String getScenicName() {
        return scenicName;
    }

    public void setScenicName(String scenicName) {
        this.scenicName = scenicName;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public List<UserPostDto> getPosts() {
        return posts;
    }

    public void setPosts(List<UserPostDto> posts) {
        this.posts = posts;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
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
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return emailVerified == userDto.emailVerified && phoneNumberVerified == userDto.phoneNumberVerified && Objects.equals(id, userDto.id) && Objects.equals(no, userDto.no) && Objects.equals(givenName, userDto.givenName) && Objects.equals(familyName, userDto.familyName) && Objects.equals(middleName, userDto.middleName) && Objects.equals(profile, userDto.profile) && Objects.equals(website, userDto.website) && gender == userDto.gender && Objects.equals(username, userDto.username) && Objects.equals(fullname, userDto.fullname) && Objects.equals(password, userDto.password) && Objects.equals(enabled, userDto.enabled) && Objects.equals(email, userDto.email) && Objects.equals(mobile, userDto.mobile) && Objects.equals(description, userDto.description) && Objects.equals(roles, userDto.roles) && Objects.equals(scenicName, userDto.scenicName) && Objects.equals(regionCode, userDto.regionCode) && Objects.equals(posts, userDto.posts) && Objects.equals(birthDate, userDto.birthDate) && Objects.equals(profilePhoto, userDto.profilePhoto) && Objects.equals(zoneinfo, userDto.zoneinfo) && Objects.equals(local, userDto.local) && Objects.equals(address, userDto.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, no, givenName, familyName, middleName, profile, website, gender, username, fullname, password, enabled, email, emailVerified, mobile, phoneNumberVerified, description, roles, scenicName, regionCode, posts, birthDate, profilePhoto, zoneinfo, local, address);
    }
}

