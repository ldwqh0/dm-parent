package com.dm.uap.dto;

import java.io.Serializable;
import java.util.List;

import com.dm.common.dto.IdentifiableDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(allowSetters = true, value = { "password" })
public class UserDto implements Serializable, IdentifiableDto<Long> {

    private static final long serialVersionUID = 3115204474399309007L;

    private Long id;
    private String username;
    private String fullname;
    private String password;
    private Boolean enabled;
    private String email;
    private String mobile;
    private String description;
    private List<RoleDto> roles;
    private String scenicName;
    private String regionCode;

    /**
     * 用户的职务信息
     */
    private List<UserPostDto> posts;
}
