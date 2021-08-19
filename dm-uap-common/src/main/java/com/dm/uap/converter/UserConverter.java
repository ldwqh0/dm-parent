package com.dm.uap.converter;

import com.dm.collections.Maps;
import com.dm.collections.Sets;
import com.dm.common.converter.Converter;
import com.dm.uap.dto.UserDto;
import com.dm.uap.dto.UserPostDto;
import com.dm.uap.entity.Department;
import com.dm.uap.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class UserConverter implements Converter<User, UserDto> {

    private final DepartmentConverter departmentConverter;

    private final UserRoleConverter userRoleConverter;

    private UserDto toDtoActual(User user) {
        UserDto dto = toSimpleDto(user);
        dto.setDescription(user.getDescription());
        Map<Department, String> _posts = user.getPosts();
        if (Maps.isNotEmpty(_posts)) {
            List<UserPostDto> posts = new ArrayList<>();
            _posts.forEach((key, value) -> posts.add(new UserPostDto(departmentConverter.toDto(key), value)));
            dto.setPosts(posts);
        }
        dto.setRoles(Sets.transform(user.getRoles(), userRoleConverter::toDto));
        return dto;
    }

    @Override
    public User copyProperties(User user, UserDto userDto) {
        if (Objects.nonNull(user) && Objects.nonNull(userDto)) {
            user.setEnabled(userDto.getEnabled());
            user.setUsername(userDto.getUsername());
            user.setFullname(userDto.getFullname());
            user.setProfilePhoto(userDto.getProfilePhoto());
            user.setMobile(userDto.getMobile());
            user.setDescription(userDto.getDescription());
            user.setEmail(userDto.getEmail());
            user.setScenicName(userDto.getScenicName());
            user.setRegionCode(userDto.getRegionCode());
            user.setBirthDate(userDto.getBirthDate());
            user.setNo(userDto.getNo());
        }
        return user;
    }

    @Override
    public UserDto toDto(User model) {
        return Optional.ofNullable(model).map(this::toDtoActual).orElse(null);
    }

    public UserDto toSimpleDto(User model) {
        UserDto dto = new UserDto();
        dto.setId(model.getId());
        dto.setFullname(model.getFullname());
        dto.setUsername(model.getUsername());
        dto.setMobile(model.getMobile());
        dto.setPhoneNumberVerified(model.isPhoneNumberVerified());
        dto.setEmail(model.getEmail());
        dto.setEmailVerified(model.isEmailVerified());
        dto.setEnabled(model.isEnabled());
        dto.setProfilePhoto(model.getProfilePhoto());
        dto.setNo(model.getNo());
        dto.setDescription(model.getDescription());
        dto.setScenicName(model.getScenicName());
        dto.setRegionCode(model.getRegionCode());
        dto.setBirthDate(model.getBirthDate());
        dto.setAddress(model.getAddress());
        dto.setGivenName(model.getGivenName());
        dto.setFamilyName(model.getFamilyName());
        dto.setMiddleName(model.getMiddleName());
        // TODO 待处理
        return dto;
    }
}
