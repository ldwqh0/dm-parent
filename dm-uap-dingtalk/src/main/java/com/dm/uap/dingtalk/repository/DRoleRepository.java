package com.dm.uap.dingtalk.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.dm.uap.dingtalk.entity.DRole;

public interface DRoleRepository extends JpaRepository<DRole, Long>, QuerydslPredicateExecutor<DRole> {

    /**
     * 删除ID不在列表中中的角色
     * 
     * @param ids
     */
    public long deleteByIdNotIn(Collection<Long> ids);

    public Optional<DRole> findByRoleId(Long roleID);

}
