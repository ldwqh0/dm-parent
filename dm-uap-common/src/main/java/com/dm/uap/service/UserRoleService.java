package com.dm.uap.service;

import com.dm.uap.dto.RoleDto;

public interface UserRoleService {

    RoleDto update(Long id, RoleDto role);

    void delete(Long id);
}
