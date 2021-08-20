package com.dm.uap.dingtalk.repository;

import com.dm.uap.dingtalk.entity.CorpLongId;
import com.dm.uap.dingtalk.entity.DRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface DRoleRepository extends JpaRepository<DRole, CorpLongId>, QuerydslPredicateExecutor<DRole> {

    @Query("update DRole set deleted=:deleted where corpId=:corpId and id not in (:ids)")
    @Modifying
    int setDeletedByCorpIdAndIdNotIn(@Param("corpId") String corpId, @Param("ids") Collection<Long> ids, @Param("deleted") Boolean deleted);

    Optional<DRole> findByRoleId(Long roleID);

    /**
     * 根据钉钉角色是否被禁用查找钉钉角色对应的系统角色ID
     *
     * @param deleted 删除状态
     * @return 角色的ID列表
     */
    @Query("select dr.role.id from DRole dr where dr.corpId=:corpId and dr.deleted=:deleted")
    List<Long> findRoleIdByCorpIdAndDRoleDeleted(@Param("corpId") String corpId, @Param("deleted") Boolean deleted);

    default DRole getById(String corpId, Long id) {
        return getById(new CorpLongId(corpId, id));
    }


    default boolean existsById(String corpId, Long id) {
        return existsById(new CorpLongId(corpId, id));
    }

    default void deleteById(String corpId, Long id) {
        deleteById(new CorpLongId(corpId, id));
    }

    List<DRole> findByCorpIdAndDeleted(String corpId, Boolean deleted);

}
