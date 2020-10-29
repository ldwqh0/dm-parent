package com.dm.uap.repository;

import com.dm.uap.entity.Role;
import com.dm.uap.entity.Role.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long>, QuerydslPredicateExecutor<Role> {
    List<Role> findByState(Status enabled);

    Optional<Role> findByName(String name);

    @Query("update Role set state=:state where id in (:deletedIds)")
    @Modifying
    int batchSetState(@Param("deletedIds") List<Long> deletedIds, @Param("state") Status disabled);

}
