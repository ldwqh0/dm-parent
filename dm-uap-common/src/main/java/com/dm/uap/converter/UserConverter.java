package com.dm.uap.converter;

import java.util.ArrayList;
import java.util.Collections;
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
import com.dm.uap.dto.UserPostDto;
import com.dm.uap.entity.Department;
import com.dm.uap.entity.Role;
import com.dm.uap.entity.User;

@Component
public class UserConverter extends AbstractConverter<User, UserDto> {

    @Autowired
    private RoleConverter roleConverter;

    @Autowired
    private DepartmentConverter departmentConverter;

    public <T extends User> UserDetailsDto toUserDetailsDto(Optional<T> user) {
        return user.map(this::toUserDetailsDto).orElse(null);
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
            List<UserPostDto> posts = new ArrayList<>();
            _posts.entrySet().forEach(a -> {
                posts.add(new UserPostDto(departmentConverter.toDto(a.getKey()).orElse(null), a.getValue()));
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
