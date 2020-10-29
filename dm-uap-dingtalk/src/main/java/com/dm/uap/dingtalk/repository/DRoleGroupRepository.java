package com.dm.uap.dingtalk.repository;

import com.dm.uap.dingtalk.entity.CorpLongId;
import com.dm.uap.dingtalk.entity.DRoleGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface DRoleGroupRepository extends JpaRepository<DRoleGroup, CorpLongId> {

    @Query("update DRoleGroup set deleted=:deleted where corpId=:corpId and id not in (:ids)")
    @Modifying
    int setDeletedByCorpidAndIdNotIn(@Param("corpId") String corpId, @Param("ids") Collection<Long> ids, @Param("deleted") Boolean deleted);

    default void deleteById(String corpId, Long id) {
        deleteById(new CorpLongId(corpId, id));
    }

    default DRoleGroup getOne(String corpId, Long id) {
        return getOne(new CorpLongId(corpId, id));
    }

    default Optional<DRoleGroup> findById(String corpId, Long id) {
        return findById(new CorpLongId(corpId, id));
    }

    default boolean existsById(String corpId, Long id) {
        return existsById(new CorpLongId(corpId, id));
    }

    List<DRoleGroup> findByCorpIdAndDeleted(String corpId, Boolean deleted);
}
