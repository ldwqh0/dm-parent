package com.dm.uap.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dm.uap.dto.RoleGroupDto;
import com.dm.uap.entity.RoleGroup;

public interface RoleGroupService {

    /**
     * 判断是否有角色组存在，这个仅用于数据初始化检测
     * 
     * @return
     */
    boolean exist();

    RoleGroup save(RoleGroupDto data);

    Optional<RoleGroup> findById(Long id);

    void deleteById(Long id);

    RoleGroup update(Long id, RoleGroupDto data);

    Page<RoleGroup> search(String key, Pageable pageable);

    Optional<RoleGroup> findByName(String string);
}
