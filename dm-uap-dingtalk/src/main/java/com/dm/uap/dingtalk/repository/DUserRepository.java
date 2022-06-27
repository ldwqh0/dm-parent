package com.dm.uap.dingtalk.repository;

import com.dm.uap.dingtalk.entity.DUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface DUserRepository extends JpaRepository<DUser, String>, QuerydslPredicateExecutor<DUser> {

    @Query("update DUser du set du.deleted=:deleted where du.deleted <> :deleted or du.deleted is null and du.userid not in(:userIds)")
    @Modifying
    int setDeletedByUseridNotIn(@Param("userIds") Collection<String> userIds, @Param("deleted") boolean deleted);

    @Query("update DUser du set du.deleted=:deleted where du.deleted <> :deleted or du.deleted is null and du.userid in(:userIds)")
    @Modifying
    int setDeletedByUseridIn(@Param("userIds") Collection<String> userIds, @Param("deleted") boolean deleted);

    @Query("select du.user.id from DUser du where du.deleted=?1")
    List<Long> findUserIdsByDUserDeleted(boolean b);

}
