package com.dm.uap.converter;

import com.dm.collections.Maps;
import com.dm.collections.Sets;
import com.dm.security.core.userdetails.UserDetailsDto;
import com.dm.uap.dto.UserDto;
import com.dm.uap.dto.UserPostDto;
import com.dm.uap.entity.Department;
import com.dm.uap.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public final class UserConverter {
    private UserConverter() {
    }

    public static UserDto toDto(User model) {
        return Optional.ofNullable(model).map(user -> {
            UserDto dto = toSimpleDto(user);
            dto.setDescription(user.getDescription());
            Map<Department, String> _posts = user.getPosts();
            if (Maps.isNotEmpty(_posts)) {
                List<UserPostDto> posts = new ArrayList<>();
                _posts.forEach((key, value) -> posts.add(new UserPostDto(DepartmentConverter.toDto(key), value)));
                dto.setPosts(posts);
            }
            dto.setRoles(Sets.transform(user.getRoles(), RoleConverter::toDto));
            return dto;
        }).orElse(null);
    }

    public static UserDto toSimpleDto(User model) {
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

    public static <T extends User> UserDetails toUserDetails(T user) {
        UserDetailsDto dto = new UserDetailsDto();
        dto.setPassword(user.getPassword());
        dto.setAccountExpired(user.isAccountExpired());
        dto.setCredentialsExpired(user.isCredentialsExpired());
        dto.setEnabled(user.isEnabled());
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setFullname(user.getFullname());
        dto.setScenicName(user.getScenicName());
        dto.setRegionCode(user.getRegionCode());
        dto.setGrantedAuthority(Sets.transform(user.getRoles(), RoleConverter::toGrantedAuthority));
        dto.setMobile(user.getMobile());
        return dto;
    }
}
