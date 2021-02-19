package com.dm.auth.repository;

import com.dm.auth.entity.AuthResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ResourceRepository extends JpaRepository<AuthResource, Long>, QuerydslPredicateExecutor<AuthResource> {

    Optional<AuthResource> findByName(String name);

    List<AuthResource> findByIdNotIn(Collection<Long> ids);

    /**
     * 获取所有资源的Scope
     *
     * @return 所有资源的scope列表
     */
    @Query("select distinct scope from AuthResource ar join ar.scope scope")
    List<String> listScopes();

}
