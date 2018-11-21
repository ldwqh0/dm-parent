package com.dm.uap.converter;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dm.common.converter.AbstractConverter;
import com.dm.security.core.userdetails.UserDetailsDto;
import com.dm.uap.dto.UserDto;
import com.dm.uap.entity.RegionInfo;
import com.dm.uap.entity.Role;
import com.dm.uap.entity.User;

@Component
public class UserConverter extends AbstractConverter<User, UserDto> {

	@Autowired
	private RoleConverter roleConverter;

	public UserDetailsDto toUserDetailsDto(Optional<User> user) {
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
			List<Role> roles = user_.getRoles();
			RegionInfo region = user_.getRegion();
			if (!Objects.isNull(region)) {
				dto.setRegion(region.asList());
			}
			if (CollectionUtils.isNotEmpty(roles)) {
				dto.setGrantedAuthority(roleConverter.toGrantedAuthorityDto(user_.getRoles()));
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
		dto.setDescribe(user.getDescribe());
		List<Role> roles = user.getRoles();
		if (CollectionUtils.isNotEmpty(roles)) {
			dto.setRoles(roleConverter.toSimpleDto(user.getRoles()));
		}
		if (!Objects.isNull(user.getRegion())) {
			dto.setRegion(user.getRegion().asList());
		}
		return dto;
	}

	@Override
	public void copyProperties(User user, UserDto userDto) {
		if (!Objects.isNull(user) && !Objects.isNull(userDto)) {
			user.setEnabled(userDto.getEnabled());
			user.setUsername(userDto.getUsername());
			user.setFullname(userDto.getFullname());
			user.setMobile(userDto.getMobile());
			user.setDescribe(userDto.getDescribe());
			user.setEmail(userDto.getEmail());
			user.setRegion(RegionInfo.fromList(userDto.getRegion()));
		}
	}

}
