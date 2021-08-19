package com.dm.uap.service.impl;

import com.dm.uap.converter.UserRoleConverter;
import com.dm.uap.dto.RoleDto;
import com.dm.uap.entity.UserRole;
import com.dm.uap.repository.UserRoleRepository;
import com.dm.uap.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userRoleService")
@RequiredArgsConstructor
public class RoleServiceImpl implements UserRoleService {

    private final UserRoleRepository userRoleRepository;

    private final UserRoleConverter userRoleConverter;

    @Override
    @Transactional
    @CacheEvict(cacheNames = {"users"}, allEntries = true)
    public RoleDto update(Long id, RoleDto dto) {
        UserRole role = userRoleConverter.copyProperties(userRoleRepository.getOne(id), dto);
        role = userRoleRepository.save(role);
        return userRoleConverter.toDto(role);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {"users"}, allEntries = true)
    public void delete(Long id) {
        userRoleRepository.deleteById(id);
    }
}
