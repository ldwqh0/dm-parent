package com.dm.uap.dingtalk.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.dm.uap.dingtalk.entity.DUser;

public interface DUserRepository extends JpaRepository<DUser, String>, QuerydslPredicateExecutor<DUser> {

    @Modifying
    @Deprecated
    public long deleteByUseridNotIn(Set<String> userIds);

    @Modifying
    @Query(value = "update DUser set deleted=true where userid not in (:userids)")
    public long setDeletedByUseridNotUd(Set<String> userids);

    @Deprecated
    public List<DUser> findByUseridNotInAndDeletedFalse(Set<String> userIds);

}
