package com.dm.uap.dto;

import java.io.Serializable;
import java.util.List;

import com.dm.uap.entity.Role.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;

/**
 * 角色数据结构
 * 
 * @author LiDong
 *
 */
@Data
@JsonInclude(value = Include.NON_EMPTY)
public class RoleDto implements Serializable {
	private static final long serialVersionUID = 4725729366179649819L;
	private Long id;
	private String name;
	private String description;
	private Status state;
	private RoleGroupDto group;
	@JsonProperty(access = Access.WRITE_ONLY)
	private List<UserDto> users;

	@JsonIgnoreProperties({ "password", "roles" })
	public List<UserDto> getUsers() {
		return users;
	}

}
