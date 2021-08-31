package com.dm.uap.service;


import com.dm.uap.dto.UserRoleDto;

public interface UserRoleService {

    UserRoleDto update(Long id, UserRoleDto role);

    void delete(Long id);
}
