package com.dm.uap.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.dm.uap.entity.Role;
import com.dm.uap.entity.Role.Status;

public interface RoleRepository extends JpaRepository<Role, Long>, QuerydslPredicateExecutor<Role> {
    public List<Role> findByState(Status enabled);

    public Optional<Role> findByName(String name);

    @Query("update Role set state=?2 where id in (?1)")
    @Modifying
    public int batchSetState(List<Long> deletedIds, Status disabled);

}
