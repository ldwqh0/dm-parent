package com.dm.uap.dto;

import com.dm.common.dto.IdentifiableDto;
import com.dm.common.validation.constraints.Mobile;
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
import java.util.List;

@Data
@JsonInclude(value = Include.NON_NULL)
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

    @NotNull(groups = {New.class, Update.class})
    @Size(max = 50, groups = {Default.class})
    private String username;

    @NotNull(groups = {New.class, Update.class})
    @Size(max = 200, groups = {Default.class})
    private String fullname;

    @NotNull(groups = {New.class})
    @Size(min = 6, max = 100, groups = {Default.class})
    private String password;

    @NotNull(groups = {New.class, Update.class})
    private Boolean enabled = Boolean.FALSE;

    @Email(groups = {Default.class})
    @Size(max = 100, groups = {Default.class})
    private String email;

    @Mobile(groups = {Default.class})
    @Size(max = 20, groups = {Default.class})
    private String mobile;

    @Size(max = 2000, groups = {Default.class})
    private String description;

    @Valid
    @NotEmpty(groups = {New.class, Update.class})
    private List<RoleDto> roles;

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
}

