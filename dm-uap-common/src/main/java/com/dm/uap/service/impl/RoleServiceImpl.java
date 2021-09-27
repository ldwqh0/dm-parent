package com.dm.uap.service.impl;

import com.dm.uap.converter.RoleConverter;
import com.dm.uap.dto.UserRoleDto;
import com.dm.uap.entity.UserRole;
import com.dm.uap.repository.UserRoleRepository;
import com.dm.uap.service.UserRoleService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//@Service("userRoleService")
public class RoleServiceImpl implements UserRoleService {

    private final UserRoleRepository userRoleRepository;

    public RoleServiceImpl(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {"users"}, allEntries = true)
    public UserRoleDto update(Long id, UserRoleDto dto) {
        return RoleConverter.toDto(userRoleRepository.save(copyProperties(userRoleRepository.getById(id), dto)));
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {"users"}, allEntries = true)
    public void delete(Long id) {
        userRoleRepository.deleteById(id);
    }


    private UserRole copyProperties(UserRole model, UserRoleDto dto) {
        model.setId(dto.getId());
        model.setName(dto.getName());
        model.setGroup(dto.getGroup());
        return model;
    }
}
