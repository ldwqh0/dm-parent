package com.dm.uap.dingtalk.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.dm.uap.dingtalk.entity.DUser;

public interface DUserRepository extends JpaRepository<DUser, String>, QuerydslPredicateExecutor<DUser> {

    @Query("update DUser set deleted=true where deleted != true and id not in(?1)")
    @Modifying
    public int setDeletedByUseridNotIn(Collection<String> userIds);

    public List<Long> findUserIdsByDUserDeleted(boolean b);

}
