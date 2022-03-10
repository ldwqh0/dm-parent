package com.dm.uap.dto;

import com.dm.collections.CollectionUtils;
import com.dm.common.validation.constraints.Mobile;
import com.dm.data.domain.Identifiable;
import com.dm.uap.entity.Address;
import com.dm.uap.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_ABSENT;
import static java.lang.Boolean.TRUE;
import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableSet;

@JsonInclude(NON_ABSENT)
@JsonIgnoreProperties(allowSetters = true, value = {"password"})
public class UserDto implements Serializable, Identifiable<Long> {

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
    private final boolean enabled;

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

    private UserDto(Long id,
                    String no,
                    String givenName,
                    String familyName,
                    String middleName,
                    String profile,
                    String website,
                    User.Gender gender,
                    String username,
                    String fullName,
                    String password,
                    Boolean enabled,
                    String email,
                    Boolean emailVerified,
                    String mobile,
                    Boolean phoneNumberVerified,
                    String description,
                    Set<UserRoleDto> roles,
                    String scenicName,
                    String regionCode,
                    List<UserPostDto> posts,
                    LocalDate birthDate,
                    String profilePhoto,
                    String zoneinfo,
                    String local,
                    Address address) {
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
        this.enabled = TRUE.equals(enabled);
        this.email = email;
        this.emailVerified = TRUE.equals(emailVerified);
        this.mobile = mobile;
        this.phoneNumberVerified = TRUE.equals(phoneNumberVerified);
        this.description = description;
        CollectionUtils.addAll(this.roles, roles);
        this.scenicName = scenicName;
        this.regionCode = regionCode;
        CollectionUtils.addAll(this.posts,posts);
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

    public boolean isEnabled() {
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

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Long id;
        private String no;
        private String givenName;
        private String familyName;
        private String middleName;
        private String profile;
        private String website;
        private User.Gender gender;
        private String username;
        private String fullName;
        private String password;
        private Boolean enabled = Boolean.FALSE;
        private String email;
        private Boolean emailVerified;
        private String mobile;
        private Boolean phoneNumberVerified;
        private String description;
        private Set<UserRoleDto> roles = new HashSet<>();
        private String scenicName;
        private String regionCode;
        private List<UserPostDto> posts = new ArrayList<>();
        private LocalDate birthDate;
        private String profilePhoto;
        private String zoneinfo;
        private String local;
        private Address address;

        private Builder() {
        }

        public static Builder anUserDto() {
            return new Builder();
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder no(String no) {
            this.no = no;
            return this;
        }

        public Builder givenName(String givenName) {
            this.givenName = givenName;
            return this;
        }

        public Builder familyName(String familyName) {
            this.familyName = familyName;
            return this;
        }

        public Builder middleName(String middleName) {
            this.middleName = middleName;
            return this;
        }

        public Builder profile(String profile) {
            this.profile = profile;
            return this;
        }

        public Builder website(String website) {
            this.website = website;
            return this;
        }

        public Builder gender(User.Gender gender) {
            this.gender = gender;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder enabled(Boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder emailVerified(boolean emailVerified) {
            this.emailVerified = emailVerified;
            return this;
        }

        public Builder mobile(String mobile) {
            this.mobile = mobile;
            return this;
        }

        public Builder phoneNumberVerified(boolean phoneNumberVerified) {
            this.phoneNumberVerified = phoneNumberVerified;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder roles(Set<UserRoleDto> roles) {
            this.roles = roles;
            return this;
        }

        public Builder scenicName(String scenicName) {
            this.scenicName = scenicName;
            return this;
        }

        public Builder regionCode(String regionCode) {
            this.regionCode = regionCode;
            return this;
        }

        public Builder posts(List<UserPostDto> posts) {
            this.posts = posts;
            return this;
        }

        public Builder birthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public Builder profilePhoto(String profilePhoto) {
            this.profilePhoto = profilePhoto;
            return this;
        }

        public Builder zoneinfo(String zoneinfo) {
            this.zoneinfo = zoneinfo;
            return this;
        }

        public Builder local(String local) {
            this.local = local;
            return this;
        }

        public Builder address(Address address) {
            this.address = address;
            return this;
        }

        public UserDto build() {
            return new UserDto(id, no, givenName, familyName, middleName, profile, website, gender, username, fullName, password, enabled, email, emailVerified, mobile, phoneNumberVerified, description, roles, scenicName, regionCode, posts, birthDate, profilePhoto, zoneinfo, local, address);
        }
    }
}

