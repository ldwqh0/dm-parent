package com.dm.uap.dingtalk.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dm.uap.dingtalk.entity.DRoleGroup;

public interface DRoleGroupRepository extends JpaRepository<DRoleGroup, Long> {

    public void deleteByIdNotIn(List<Long> collect);

    public List<DRoleGroup> findByIdNotInAndDeletedFalse(Collection<Long> ids);
}
