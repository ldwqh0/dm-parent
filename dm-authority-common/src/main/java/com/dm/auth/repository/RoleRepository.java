package com.dm.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.dm.auth.entity.Menu;
import com.dm.auth.entity.Role;
import com.dm.auth.entity.Role.Status;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long>, QuerydslPredicateExecutor<Role> {

    List<Role> findByState(Status enabled);

    Optional<Role> findByGroupAndName(String group, String name);

    List<Role> findByMenu(Menu menu);

    @Query("update Role set state=:state where id in (:deletedIds)")
    @Modifying
    int batchSetState(@Param("deletedIds") List<Long> deletedIds, @Param("state") Status disabled);

    @Query("select r from Role r join r.resourceOperations ro where KEY(ro).id=:resourceId")
    List<Role> findByResourceOperationsResourceId(@Param("resourceId") Long resourceId);

}
