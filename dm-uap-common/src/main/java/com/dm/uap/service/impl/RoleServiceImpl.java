package com.dm.uap.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dm.uap.converter.RoleConverter;
import com.dm.uap.dto.RoleDto;
import com.dm.uap.dto.RoleGroupDto;
import com.dm.uap.entity.QRole;
import com.dm.uap.entity.Role;
import com.dm.uap.entity.Role.Status;
import com.dm.uap.entity.RoleGroup;
import com.dm.uap.repository.RoleGroupRepository;
import com.dm.uap.repository.RoleRepository;
import com.dm.uap.repository.UserRepository;
import com.dm.uap.service.RoleService;
import com.querydsl.core.BooleanBuilder;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleConverter roleConverter;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleGroupRepository roleGroupRepository;

    private final QRole qRole = QRole.role;

    @Override
    public boolean exist() {
        return roleRepository.count() > 0;
    }

    @Override
    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    @Transactional
    public Role save(RoleDto roleDto) {
        Role role = new Role();
        copyProperties(role, roleDto);
        return roleRepository.save(role);
    }

    @Override
    public boolean nameExist(Long id, String name) {
        BooleanBuilder builder = new BooleanBuilder();
        if (id != null) {
            builder.and(qRole.id.ne(id));
        }
        builder.and(qRole.name.eq(name));
        return roleRepository.exists(builder);
    }

    @Override
    @Transactional
    public Role update(long id, RoleDto roleDto) {
        Role role = roleRepository.getOne(id);
        copyProperties(role, roleDto);
        return role;
    }

    @Override
    public Optional<Role> get(long id) {
        return roleRepository.findById(id);
    }

    @Override
    @Transactional
    public void delete(long id) {
        roleRepository.deleteById(id);
    }

    @Override
    public Page<Role> search(Long groupId, String key, Pageable pageable) {
        BooleanBuilder query = new BooleanBuilder();
        if (!Objects.isNull(groupId)) {
            query.and(qRole.group.id.eq(groupId));
        }
        if (StringUtils.isNotBlank(key)) {
            query.and(qRole.name.containsIgnoreCase(key).or(qRole.description.containsIgnoreCase(key)));
            return roleRepository.findAll(query, pageable);
        }
        return roleRepository.findAll(query, pageable);
    }

    @Override
    public List<Role> listAllEnabled() {
        return roleRepository.findByState(Status.ENABLED);
    }

    private void copyProperties(Role data, RoleDto dto) {
        roleConverter.copyProperties(data, dto);
        data.setUsers(userRepository.getByDto(dto.getUsers()));
        RoleGroupDto _group = dto.getGroup();
        if (Objects.isNull(_group)) {
            data.setGroup(null);
        } else {
            Long groupId = _group.getId();
            String groupName = _group.getName();
            if (!Objects.isNull(groupId)) {
                data.setGroup(roleGroupRepository.getOne(groupId));
            } else if (StringUtils.isNotBlank(groupName)) {
                RoleGroup group = new RoleGroup(groupName);
                data.setGroup(roleGroupRepository.save(group));
            }
        }
    }
}
