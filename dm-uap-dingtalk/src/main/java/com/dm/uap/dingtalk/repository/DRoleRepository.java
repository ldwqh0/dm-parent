package com.dm.uap.dingtalk.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.dm.uap.dingtalk.entity.CorpLongId;
import com.dm.uap.dingtalk.entity.DRole;

public interface DRoleRepository extends JpaRepository<DRole, CorpLongId>, QuerydslPredicateExecutor<DRole> {

    @Query("update DRole set deleted=?3 where corpId=?1 and deleted !=?3 and id not in (?2)")
    @Modifying
    public int setDeletedByCorpidAndIdNotIn(String corpid, Collection<Long> ids, Boolean deleted);

    public Optional<DRole> findByRoleId(Long roleID);

    /**
     * 根据钉钉角色是否被禁用查找钉钉角色对应的系统角色ID
     * 
     * @param deleted
     * @return
     */
    @Query("select dr.role.id from DRole dr where dr.corpId=?1 and dr.deleted=?2")
    public List<Long> findRoleIdByCorpidAndDRoleDeleted(String corpid, Boolean deleted);

    public default DRole getOne(String corpid, Long id) {
        return getOne(new CorpLongId(corpid, id));
    };

    public default boolean existsById(String corpid, Long id) {
        return existsById(new CorpLongId(corpid, id));
    }

}
