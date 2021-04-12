package com.dm.uap.service.impl;

import com.dm.uap.entity.Role;
import com.dm.uap.repository.UserRepository;
import com.dm.uap.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    @CacheEvict(cacheNames = {"users"}, allEntries = true)
    public int update(Long id, Role role) {
        return userRepository.updateRole(id, role.getName(), role.getGroup());
    }
}
