package com.dm.uap.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class RoleGroupDto implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String description;
//	private List<RoleDto> roles;
}
