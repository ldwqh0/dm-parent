package com.dm.uap.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.dm.uap.dto.RoleDto;
import com.dm.uap.entity.Role;

public interface RoleService {

    boolean exist();

    Role save(RoleDto dto);

    boolean nameExist(Long id, String name);

    Role update(long id, RoleDto roleDto);

    Optional<Role> get(long id);

    void delete(long id);

    Page<Role> search(Long groupId, String key, Pageable pageable);

    List<Role> listAllEnabled();

    Optional<Role> findByName(String string);

}
