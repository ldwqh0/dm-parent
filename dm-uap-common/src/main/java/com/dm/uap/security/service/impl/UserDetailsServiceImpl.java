package com.dm.uap.security.service.impl;

import com.dm.collections.Sets;
import com.dm.security.core.userdetails.GrantedAuthorityDto;
import com.dm.security.core.userdetails.UserDetailsDto;
import com.dm.uap.entity.User;
import com.dm.uap.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    private final GrantedAuthorityDto defaultGrantedAuthority = new GrantedAuthorityDto(1L, "内置分组_ROLE_AUTHENTICATED");

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "users", sync = true, key = "#username.toLowerCase()")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return Optional.ofNullable(username)
            .filter(StringUtils::isNotEmpty)
            .flatMap(userRepository::findOneByUsernameIgnoreCase)
            .map(this::toUserDetails)
            .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "users", sync = true, key = "'M@_' + #result.mobile.toLowerCase()", condition = "#result.mobile!=null")
    public UserDetails loadUserByMobile(String mobile) throws UsernameNotFoundException {
        return Optional.ofNullable(mobile)
            .filter(StringUtils::isNotEmpty)
            .flatMap(userRepository::findByMobileIgnoreCase)
            .map(this::toUserDetails)
            .orElseThrow(() -> new UsernameNotFoundException(mobile));
    }

    private UserDetails toUserDetails(User user) {
        Set<GrantedAuthorityDto> grantedAuthorities = new HashSet<>(Sets.transform(user.getRoles(), userRole -> new GrantedAuthorityDto(userRole.getId(), userRole.getGroup() + "_" + userRole.getName())));
        grantedAuthorities.add(defaultGrantedAuthority);
        return UserDetailsDto.builder()
            .id(user.getId())
            .username(user.getUsername())
            .password(user.getPassword())
            .accountExpired(user.isAccountExpired())
            .credentialsExpired(user.isCredentialsExpired())
            .enabled(user.isEnabled())
            .locked(false)
            .grantedAuthority(grantedAuthorities)
            .fullName(user.getFullName())
            .mobile(user.getMobile())
            .email(user.getEmail())
            .regionCode(user.getRegionCode())
            .scenicName(user.getScenicName())
            .build();
    }
}
