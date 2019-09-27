package com.dm.uap.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dm.uap.dto.RoleDto;
import com.dm.uap.entity.Role;

public interface RoleService {

	public boolean exist();

	public Role save(RoleDto dto);

	public Optional<Role> getFirst();

	public boolean nameExist(Long id, String name);

	public Role update(long id, RoleDto roleDto);

	public Optional<Role> get(long id);

	public void delete(long id);

	public Page<Role> search(Long groupId, String key, Pageable pageable);

	public List<Role> listAllEnabled();

	public Optional<Role> findByName(String string);

}
