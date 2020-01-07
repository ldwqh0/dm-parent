package com.dm.uap.dingtalk.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.dm.uap.dingtalk.entity.DRoleGroup;

public interface DRoleGroupRepository extends JpaRepository<DRoleGroup, Long> {

    @Query("update DRoleGroup set deleted=true where deleted != true and id not in (?1)")
    @Modifying
    public int setDeletedByIdNotIn(Collection<Long> ids);

}
