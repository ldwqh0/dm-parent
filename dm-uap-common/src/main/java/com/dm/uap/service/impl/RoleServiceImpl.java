package com.dm.uap.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dm.uap.converter.RoleConverter;
import com.dm.uap.dto.RoleDto;
import com.dm.uap.dto.UserDto;
import com.dm.uap.entity.QRole;
import com.dm.uap.entity.Role;
import com.dm.uap.entity.Role.Status;
import com.dm.uap.entity.User;
import com.dm.uap.repository.RoleRepository;
import com.dm.uap.repository.UserRepository;
import com.dm.uap.service.RoleService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleConverter roleConverter;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	private final QRole qRole = QRole.role;

	@Override
	public boolean exist() {
		return roleRepository.count() > 0;
	}

	@Override
	@Transactional
	public Role save(RoleDto roleDto) {
		Role role = new Role();
		roleConverter.copyProperties(role, roleDto);
		role.setUsers(getUsersFromDto(roleDto.getUsers()));
		return roleRepository.save(role);
	}

	@Override
	public Optional<Role> getFirst() {
		PageRequest page = PageRequest.of(0, 1);
		return roleRepository.findAll(page).stream().findFirst();
	}

	@Override
	public boolean nameExist(Long id, String name) {
		BooleanBuilder builder = new BooleanBuilder();
		if (id != null) {
			builder.and(qRole.id.ne(id));
		}
		builder.and(qRole.name.eq(name));
		return roleRepository.exists(builder.getValue());
	}

	@Override
	@Transactional
	public Role update(long id, RoleDto roleDto) {
		Role role = roleRepository.getOne(id);
		roleConverter.copyProperties(role, roleDto);
		role.setUsers(getUsersFromDto(roleDto.getUsers()));
		roleRepository.save(role);
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
	public Page<Role> search(String key, Pageable pageable) {
		if (StringUtils.isBlank(key)) {
			return roleRepository.findAll(pageable);
		} else {
			BooleanExpression exp = qRole.name.containsIgnoreCase(key).or(qRole.describe.containsIgnoreCase(key));
			return roleRepository.findAll(exp, pageable);
		}
	}

	@Override
	public List<Role> listAllEnabled() {
		return roleRepository.findByState(Status.ENABLED);
	}

	private List<User> getUsersFromDto(List<UserDto> users) {
		if (CollectionUtils.isNotEmpty(users)) {
			return users.stream().map(user -> user.getId()).map(userRepository::getOne).collect(Collectors.toList());
		} else {
			return Collections.emptyList();
		}
	}
}
