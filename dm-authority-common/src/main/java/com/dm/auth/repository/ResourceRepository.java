package com.dm.auth.repository;

import com.dm.auth.entity.AuthResource;
import com.dm.security.authentication.UriResource;
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
     * 根据资源匹配模式和资源匹配路径查找资源
     *
     * @param matcher
     * @param type
     * @return
     */
    List<AuthResource> findByMatcherAndMatchType(String matcher, UriResource.MatchType type);

    List<AuthResource> findByMatcherAndMatchTypeAndIdNotIn(String matcher, UriResource.MatchType type, Collection<Long> excludes);

    /**
     * 获取所有资源的Scope
     *
     * @return 所有资源的scope列表
     */
    @Query("select distinct scope from AuthResource ar join ar.scope scope")
    List<String> listScopes();

    @Query("select max(ar.id) from AuthResource ar")
    Optional<Long> findMaxId();

}
