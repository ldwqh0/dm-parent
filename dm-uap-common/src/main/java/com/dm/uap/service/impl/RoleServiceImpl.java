package com.dm.uap.service.impl;

import com.dm.uap.converter.RoleConverter;
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

    private final RoleConverter roleConverter;

    @Override
    @Transactional
    @CacheEvict(cacheNames = {"users"}, allEntries = true)
    public RoleDto update(Long id, RoleDto dto) {
        UserRole role = roleConverter.copyProperties(userRoleRepository.getOne(id), dto);
        role = userRoleRepository.save(role);
        return roleConverter.toDto(role);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {"users"}, allEntries = true)
    public void delete(Long id) {
        userRoleRepository.deleteById(id);
    }
}
