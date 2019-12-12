package com.dm.uap.service.impl;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dm.uap.converter.RoleGroupConverter;
import com.dm.uap.dto.RoleGroupDto;
import com.dm.uap.entity.QRoleGroup;
import com.dm.uap.entity.RoleGroup;
import com.dm.uap.repository.RoleGroupRepository;
import com.dm.uap.service.RoleGroupService;
import com.querydsl.core.types.dsl.BooleanExpression;

@Service
public class RoleGroupServiceImpl implements RoleGroupService {

    @Autowired
    private RoleGroupRepository roleGroupRepository;

    @Autowired
    private RoleGroupConverter roleGroupConverter;

    private final QRoleGroup qRoleGroup = QRoleGroup.roleGroup;

    @Override
    public boolean exist() {
        return roleGroupRepository.count() > 0;
    }

    @Override
    @Transactional
    public RoleGroup save(RoleGroupDto data) {
        RoleGroup model = new RoleGroup();
        roleGroupConverter.copyProperties(model, data);
        return roleGroupRepository.save(model);
    }

    @Override
    public Optional<RoleGroup> findById(Long id) {
        return roleGroupRepository.findById(id);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        roleGroupRepository.deleteById(id);
    }

    @Override
    @Transactional
    public RoleGroup update(Long id, RoleGroupDto data) {
        RoleGroup model = new RoleGroup();
        roleGroupConverter.copyProperties(model, data);
        return roleGroupRepository.save(model);
    }

    @Override
    public Page<RoleGroup> search(String key, Pageable pageable) {
        if (StringUtils.isNotBlank(key)) {
            BooleanExpression query = qRoleGroup.name.containsIgnoreCase(key)
                    .or(qRoleGroup.description.containsIgnoreCase(key));
            return roleGroupRepository.findAll(query, pageable);
        } else {
            return roleGroupRepository.findAll(pageable);
        }
    }

    @Override
    public Optional<RoleGroup> findByName(String name) {
        return roleGroupRepository.findByName(name);
    }

}
