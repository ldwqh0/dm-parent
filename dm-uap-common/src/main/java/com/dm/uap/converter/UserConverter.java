package com.dm.uap.converter;

import com.dm.collections.Maps;
import com.dm.collections.Sets;
import com.dm.uap.dto.UserDto;
import com.dm.uap.dto.UserPostDto;
import com.dm.uap.entity.Department;
import com.dm.uap.entity.User;

import java.util.*;


public final class UserConverter {
    private UserConverter() {
    }

    public static UserDto toDto(User model) {
        return Optional.ofNullable(model).map(user -> {
            Map<Department, String> _posts = user.getPosts();
            List<UserPostDto> posts = new ArrayList<>();
            if (Maps.isNotEmpty(_posts)) {
                _posts.forEach((key, value) -> posts.add(new UserPostDto(DepartmentConverter.toSimpleDto(key), value)));
            }

            return
                new UserDto(
                    model.getId(),
                    model.getNo(),
                    model.getGivenName(),
                    model.getFamilyName(),
                    model.getMiddleName(),
                    model.getProfile(),
                    model.getWebsite(),
                    model.getGender(),
                    model.getUsername(),
                    model.getFullName(),
                    null,
                    model.getEmail(),
                    model.isEmailVerified(),
                    model.getMobile(),
                    model.isPhoneNumberVerified(),
                    user.getDescription(),
                    Sets.transform(user.getRoles(), RoleConverter::toDto),
                    model.getScenicName(),
                    model.getRegionCode(),
                    posts,
                    model.getBirthDate(),
                    model.getProfilePhoto(),
                    model.getZoneinfo(),
                    model.getLocal(),
                    model.getAddress()
                );
        }).orElse(null);
    }

    public static UserDto toSimpleDto(User model) {
        return new UserDto(
            model.getId(),
            model.getNo(),
            model.getGivenName(),
            model.getFamilyName(),
            model.getMiddleName(),
            model.getProfile(),
            model.getWebsite(),
            model.getGender(),
            model.getUsername(),
            model.getFullName(),
            null,
            model.getEmail(),
            model.isEmailVerified(),
            model.getMobile(),
            model.isPhoneNumberVerified(),
            "",
            Collections.emptySet(),
            model.getScenicName(),
            model.getRegionCode(),
            Collections.emptyList(),
            model.getBirthDate(),
            model.getProfilePhoto(),
            model.getZoneinfo(),
            model.getLocal(),
            model.getAddress()
        );
    }


}
