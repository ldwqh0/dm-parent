package com.dm.uap.converter;

import com.dm.auth.converter.RoleConverter;
import com.dm.auth.entity.Role;
import com.dm.collections.CollectionUtils;
import com.dm.collections.Lists;
import com.dm.collections.Maps;
import com.dm.common.converter.Converter;
import com.dm.security.core.userdetails.UserDetailsDto;
import com.dm.uap.dto.UserDto;
import com.dm.uap.dto.UserPostDto;
import com.dm.uap.entity.Department;
import com.dm.uap.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UserConverter implements Converter<User, UserDto> {

    private final RoleConverter roleConverter;

    private final DepartmentConverter departmentConverter;

    @Autowired
    public UserConverter(RoleConverter roleConverter, DepartmentConverter departmentConverter) {
        this.roleConverter = roleConverter;
        this.departmentConverter = departmentConverter;
    }

    public <T extends User> UserDetailsDto toUserDetailsDto(T user) {
        UserDetailsDto dto = new UserDetailsDto();
        dto.setPassword(user.getPassword());
        dto.setAccountExpired(user.isAccountExpired());
        dto.setCredentialsExpired(user.isCredentialsExpired());
        dto.setEnabled(user.isEnabled());
        dto.setId(user.getId());
        dto.setLocked(user.isLocked());
        dto.setUsername(user.getUsername());
        dto.setFullname(user.getFullname());
        dto.setScenicName(user.getScenicName());
        dto.setRegionCode(user.getRegionCode());
        List<Role> roles = user.getRoles();
        if (CollectionUtils.isNotEmpty(roles)) {
            dto.setGrantedAuthority(roleConverter.toGrantedAuthorityDto(user.getRoles()));
        } else {
            dto.setGrantedAuthority(Collections.emptyList());
        }
        return dto;

    }

    private UserDto toDtoActual(User user) {
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
        if (Maps.isNotEmpty(_posts)) {
            List<UserPostDto> posts = new ArrayList<>();
            _posts.forEach((key, value) -> posts.add(new UserPostDto(departmentConverter.toDto(key), value)));
            dto.setPosts(posts);
        }
        List<Role> roles = user.getRoles();
        if (CollectionUtils.isNotEmpty(roles)) {
            dto.setRoles(Lists.transform(user.getRoles(), roleConverter::toDto));
        }
        return dto;
    }

    @Override
    public User copyProperties(User user, UserDto userDto) {
        if (Objects.nonNull(user) && Objects.nonNull(userDto)) {
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

    @Override
    public UserDto toDto(User model) {
        return Optional.ofNullable(model).map(this::toDtoActual).orElse(null);
    }

}
