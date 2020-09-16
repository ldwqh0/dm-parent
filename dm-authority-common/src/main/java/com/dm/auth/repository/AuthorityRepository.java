package com.dm.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.dm.auth.entity.Authority;
import com.dm.auth.entity.Menu;

public interface AuthorityRepository extends JpaRepository<Authority, String>, QuerydslPredicateExecutor<Authority> {

    public List<Authority> findByMenu(Menu menu);

    @Query("select a from Authority a join a.resourceOperations ro where KEY(ro).id=:resourceId")
    public List<Authority> findByResourceOperationsResourceId(@Param("resourceId") Long resourceId);

    public List<Authority> findByRoleNameIn(List<String> roleName);
}
