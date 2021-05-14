package com.dm.server.authorization.service

import com.dm.server.authorization.dto.ClientDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

/**
 * Copyright (C) 2019 江苏云智网络科技股份有限公司版权所有
 *
 * <p>服务器安全配置</p>
 *
 * @author 李东
 */
interface ClientService {
    fun save(client: ClientDto): ClientDto

    fun existAnyClient(): Boolean

    fun delete(clientId: String)

    fun findById(id: String): ClientDto?

    fun update(id: String, client: ClientDto): ClientDto

    fun find(key: String?, pageable: Pageable): Page<ClientDto>

    fun existsByName(name: String, exclude: String? = null): Boolean

    fun findScopes(keyword: String, pageable: Pageable): Page<String>

}
