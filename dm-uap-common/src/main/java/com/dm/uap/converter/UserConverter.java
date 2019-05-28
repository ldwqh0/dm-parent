package com.dm.uap.converter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dm.common.converter.AbstractConverter;
import com.dm.security.core.userdetails.UserDetailsDto;
import com.dm.uap.dto.UserDto;
import com.dm.uap.entity.Department;
import com.dm.uap.entity.Role;
import com.dm.uap.entity.User;

@Component
public class UserConverter extends AbstractConverter<User, UserDto> {

	@Autowired
	private RoleConverter roleConverter;

//	@Autowired
//	private DepartmentConverter departmentConverter;

	public <T extends User> UserDetailsDto toUserDetailsDto(Optional<T> user) {
		UserDetailsDto dto = null;
		if (user.isPresent()) {
			User user_ = user.get();
			dto = new UserDetailsDto();
			dto.setPassword(user_.getPassword());
			dto.setAccountExpired(user_.isAccountExpired());
			dto.setCredentialsExpired(user_.isCredentialsExpired());
			dto.setEnabled(user_.isEnabled());
			dto.setId(user_.getId());
			dto.setLocked(user_.isLocked());
			dto.setUsername(user_.getUsername());
			dto.setFullname(user_.getFullname());
			dto.setScenicName(user_.getScenicName());
			dto.setRegionCode(user_.getRegionCode());
			List<Role> roles = user_.getRoles();
			if (CollectionUtils.isNotEmpty(roles)) {
				dto.setGrantedAuthority(roleConverter.toGrantedAuthorityDto(user_.getRoles()));
			} else {
				dto.setGrantedAuthority(Collections.emptyList());
			}
		}
		return dto;
	}

	@Override
	protected UserDto toDtoActual(User user) {
		UserDto dto = new UserDto();
		dto.setEnabled(user.isEnabled());
		dto.setId(user.getId());
		dto.setUsername(user.getUsername());
		dto.setFullname(user.getFullname());
		dto.setMobile(user.getMobile());
		dto.setEmail(user.getEmail());
		dto.setDescription(user.getDescription());
		dto.setScenicName(user.getScenicName());
		dto.setRegionCode(user.getRegionCode());
		Map<Department, String> _posts = user.getPosts();
		if (MapUtils.isNotEmpty(_posts)) {
			Map<Long, String> posts = new HashMap<>();
			_posts.entrySet().forEach(a -> {
				posts.put(a.getKey().getId(), a.getValue());
			});
			dto.setPosts(posts);
		}
		List<Role> roles = user.getRoles();
		if (CollectionUtils.isNotEmpty(roles)) {
			dto.setRoles(roleConverter.toDto(user.getRoles()));
		}
		return dto;
	}

	@Override
	public User copyProperties(User user, UserDto userDto) {
		if (!Objects.isNull(user) && !Objects.isNull(userDto)) {
			user.setEnabled(userDto.getEnabled());
			user.setUsername(userDto.getUsername());
			user.setFullname(userDto.getFullname());
			user.setMobile(userDto.getMobile());
			user.setDescription(userDto.getDescription());
			user.setEmail(userDto.getEmail());
			user.setScenicName(userDto.getScenicName());
			user.setRegionCode(userDto.getRegionCode());
		}
		return user;
	}

}
