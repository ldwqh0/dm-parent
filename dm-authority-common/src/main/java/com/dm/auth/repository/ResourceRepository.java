package com.dm.auth.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.dm.auth.entity.AuthResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ResourceRepository extends JpaRepository<AuthResource, Long>, QuerydslPredicateExecutor<AuthResource> {

    public Optional<AuthResource> findByName(String name);

    public List<AuthResource> findByIdNotIn(Collection<Long> ids);

    /**
     * 获取所有资源的Scope
     * 
     * @return
     */
    public List<String> listScopes();

}
