package com.dm.uap.dto;

import com.dm.common.dto.IdentifiableDto;
import com.dm.common.validation.constraints.Mobile;
import com.dm.uap.entity.Address;
import com.dm.uap.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
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
    private Set<RoleDto> roles;

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

}

