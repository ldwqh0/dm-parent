package com.dm.uap.converter;

import com.dm.collections.Maps;
import com.dm.collections.Sets;
import com.dm.uap.dto.UserDto;
import com.dm.uap.dto.UserPostDto;
import com.dm.uap.entity.Department;
import com.dm.uap.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class UserConverter {
    private UserConverter() {
    }

    private static UserDto.Builder newBuilder(User model) {
        return UserDto.builder()
            .id(model.getId())
            .no(model.getNo())
            .givenName(model.getGivenName())
            .familyName(model.getFamilyName())
            .middleName(model.getMiddleName())
            .profile(model.getProfile())
            .website(model.getWebsite())
            .gender(model.getGender())
            .username(model.getUsername())
            .fullName(model.getFullName())
            .email(model.getEmail())
            .emailVerified(model.isEmailVerified())
            .mobile(model.getMobile())
            .phoneNumberVerified(model.isPhoneNumberVerified())
            .scenicName(model.getScenicName())
            .regionCode(model.getRegionCode())
            .birthDate(model.getBirthDate())
            .profilePhoto(model.getProfilePhoto())
            .zoneinfo(model.getZoneinfo())
            .local(model.getLocal())
            .address(model.getAddress());
    }

    public static UserDto toDto(User model) {
        return Optional.ofNullable(model).map(user -> {
            Map<Department, String> _posts = user.getPosts();
            List<UserPostDto> posts = new ArrayList<>();
            if (Maps.isNotEmpty(_posts)) {
                _posts.forEach((key, value) -> posts.add(new UserPostDto(DepartmentConverter.toSimpleDto(key), value)));
            }
            return newBuilder(user)
                .description(user.getDescription())
                .posts(posts)
                .roles(Sets.transform(user.getRoles(), RoleConverter::toDto))
                .build();

        }).orElse(null);
    }

    public static UserDto toSimpleDto(User model) {
        return newBuilder(model).build();
    }

}
