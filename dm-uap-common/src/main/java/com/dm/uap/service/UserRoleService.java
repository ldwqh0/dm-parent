package com.dm.uap.service;


import com.dm.security.core.userdetails.RoleDto;

public interface UserRoleService {

    RoleDto update(Long id, RoleDto role);

    void delete(Long id);
}
