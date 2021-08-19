package com.dm.server.authorization.repository

import com.dm.server.authorization.entity.Client
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.querydsl.QuerydslPredicateExecutor

/**
 * Copyright (C) 2019 江苏云智网络科技股份有限公司版权所有
 *
 *
 * ClientRepository
 *
 * @author 李东
 */
interface ClientRepository : JpaRepository<Client, String>, QuerydslPredicateExecutor<Client> {

    /**
     * 检测指定的client是否存在，并且排除指定的id
     */
    fun existsByNameAndIdIsNot(name: String, id: String): Boolean

    /**
     * 检测特定名称的client是否存在
     */
    fun existsByName(name: String): Boolean

    @Query("select distinct scope from Client c ,in(c.scopes) scope")
    fun findScopes(keyword: String = "", pageable: Pageable): Page<String>;
}
