package com.dm.server.authorization.dto

import com.dm.server.authorization.entity.Client

data class ClientDto(
    val id: String? = null,

    val name: String = "",

    val secret: String = "",

    val type: Client.Type = Client.Type.CLIENT,

    val accessTokenValiditySeconds: Int = 3600,

    val refreshTokenValiditySeconds: Int = 7200,

    val requirePkce: Boolean = false,

    val autoApprove: Boolean = false,

    val authorizedGrantTypes: Set<String> = setOf(),

    val scopes: Set<String> = setOf(),

    val registeredRedirectUris: Set<String> = setOf()
)
