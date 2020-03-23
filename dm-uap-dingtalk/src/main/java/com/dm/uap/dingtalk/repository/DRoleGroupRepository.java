package com.dm.uap.dingtalk.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.dm.uap.dingtalk.entity.CorpLongId;
import com.dm.uap.dingtalk.entity.DRoleGroup;

public interface DRoleGroupRepository extends JpaRepository<DRoleGroup, CorpLongId> {

    @Query("update DRoleGroup set deleted=?3 where corpId=?1 and deleted != ?3 and id not in (?2)")
    @Modifying
    public int setDeletedByIdNotIn(String corpid, Collection<Long> ids, Boolean deleted);

    public default void deleteById(String corpid, Long id) {
        deleteById(new CorpLongId(corpid, id));
    }

    public default DRoleGroup getOne(String corpid, Long id) {
        return getOne(new CorpLongId(corpid, id));
    }

    public default Optional<DRoleGroup> findById(String corpid, Long id) {
        return findById(new CorpLongId(corpid, id));
    }

    public default boolean existsById(String corpid, Long id) {
        return existsById(new CorpLongId(corpid, id));
    }
}
