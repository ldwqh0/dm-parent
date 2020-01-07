package com.dm.uap.dingtalk.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.dm.uap.dingtalk.entity.DRole;

public interface DRoleRepository extends JpaRepository<DRole, Long>, QuerydslPredicateExecutor<DRole> {

    @Query("update DRole set deleted=true where deleted !=true and id not in (?1)")
    @Modifying
    public int setDeletedByIdNotIn(Collection<Long> ids);

    public Optional<DRole> findByRoleId(Long roleID);

    /**
     * 根据钉钉角色是否被禁用查找钉钉角色对应的系统角色ID
     * 
     * @param deleted
     * @return
     */
    @Query("select dr.role.id from DRole dr where dr.deleted=?1")
    public List<Long> findRoleIdByDRoleDeleted(boolean deleted);

}
