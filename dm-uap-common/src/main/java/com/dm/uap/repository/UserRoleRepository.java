package com.dm.uap.repository;

import com.dm.common.repository.IdentifiableDtoRepository;
import com.dm.uap.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long>, IdentifiableDtoRepository<UserRole, Long> {
}
