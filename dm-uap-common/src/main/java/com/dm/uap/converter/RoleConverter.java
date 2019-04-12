package com.dm.uap.converter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dm.common.converter.AbstractConverter;
import com.dm.security.core.userdetails.GrantedAuthorityDto;
import com.dm.uap.dto.RoleDto;
import com.dm.uap.entity.Role;

@Component
public class RoleConverter extends AbstractConverter<Role, RoleDto> {

//	@Autowired
//	private UserConverter userConverter;

	@Override
	protected RoleDto toDtoActual(Role role) {
		RoleDto dto = toSimpleDto(role);
//		dto.setUsers(userConverter.toDto(role.getUsers()));
		return dto;
	}

	/**
	 * 转换为简单的角色数据对象，不包含用户信息
	 * 
	 * @param role
	 * @return
	 */
	public RoleDto toSimpleDto(Role role) {
		RoleDto dto = null;
		if (role != null) {
			dto = new RoleDto();
			dto.setId(role.getId());
			dto.setName(role.getName());
			dto.setState(role.getState());
			dto.setDescription(role.getDescription());
		}
		return dto;
	}

	public List<RoleDto> toSimpleDto(List<Role> roles) {
		if (CollectionUtils.isEmpty(roles)) {
			return Collections.emptyList();
		} else {
			return roles.stream().map(this::toSimpleDto).collect(Collectors.toList());
		}
	}

	@Override
	public void copyProperties(Role role, RoleDto roleDto) {
		if (role != null && roleDto != null) {
			role.setName(roleDto.getName());
			role.setDescription(roleDto.getDescription());
			role.setState(roleDto.getState());
		}

	}

	public List<? extends GrantedAuthorityDto> toGrantedAuthorityDto(List<Role> roles) {
		if (CollectionUtils.isNotEmpty(roles)) {
			return roles.stream().map(this::toGrantedAuthorityDto).collect(Collectors.toList());
		} else {
			return Collections.emptyList();
		}
	}

	public GrantedAuthorityDto toGrantedAuthorityDto(Role role) {
		GrantedAuthorityDto dto = new GrantedAuthorityDto();
		dto.setId(role.getId());
		dto.setAuthority(role.getGroup().getName()+ role.getName());
		return dto;
	}

}
