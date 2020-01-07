package com.dm.uap.dingtalk.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.dm.uap.dingtalk.entity.DRoleGroup;

public interface DRoleGroupRepository extends JpaRepository<DRoleGroup, Long> {

    @Modifying
    @Deprecated
    public long deleteByIdNotIn(List<Long> collect);

    @Deprecated
    public List<DRoleGroup> findByIdNotInAndDeletedFalse(Collection<Long> ids);
}
