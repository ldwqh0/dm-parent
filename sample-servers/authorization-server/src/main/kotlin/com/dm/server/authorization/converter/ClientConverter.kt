package com.dm.server.authorization.converter

import com.dm.collections.CollectionUtils
import com.dm.server.authorization.dto.ClientDto
import com.dm.server.authorization.entity.Client
import org.xyyh.authorization.client.BaseClientDetails
import org.xyyh.authorization.client.ClientDetails

object ClientConverter {
    fun copyProperties(client: Client, dto: ClientDto): Client {
        client.name = dto.name
        client.authorizedGrantTypes = dto.authorizedGrantTypes
        client.scopes = dto.scopes
        client.accessTokenValiditySeconds = dto.accessTokenValiditySeconds
        client.refreshTokenValiditySeconds = dto.refreshTokenValiditySeconds
        client.registeredRedirectUris = dto.registeredRedirectUris
        client.requirePkce = dto.requirePkce
        client.type = dto.type
        client.autoApprove = dto.autoApprove
        return client
    }

    fun toListDto(model: Client): ClientDto = ClientDto(
        model.id,
        model.name,
        // 所有的输出不包含secret
        "",
        model.type,
        model.accessTokenValiditySeconds,
        model.refreshTokenValiditySeconds,
        model.requirePkce,
        model.autoApprove
    )

    fun toDto(model: Client): ClientDto = ClientDto(
        model.id,
        model.name,
        // 所有的输出不包含secret
        "",
        model.type,
        model.accessTokenValiditySeconds,
        model.refreshTokenValiditySeconds,
        model.requirePkce,
        model.autoApprove,
        model.authorizedGrantTypes,
        model.scopes,
        model.registeredRedirectUris,
    )


    fun toClientDetails(model: Client): ClientDetails {
        val clientDetails = BaseClientDetails(
            model.id,
            model.secret,
            model.autoApprove,
            model.scopes,
            model.registeredRedirectUris,
            model.authorizedGrantTypes,
            model.accessTokenValiditySeconds,
            model.refreshTokenValiditySeconds,
            model.requirePkce
        )
        // bcd.setAccessTokenValiditySeconds(model.getAccessTokenValiditySeconds());
        // TODO bcd.setAdditionalInformation(model.getAdditionalInformation());
        if (CollectionUtils.isNotEmpty(model.authorities)) {
            // TODO
            // bcd.setAuthorities(model.getAuthorities().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet()));
        }
        // bcd.setAutoApproveScopes(autoApproveScopes);
        // TODO
        // bcd.setRefreshTokenValiditySeconds(model.getRefreshTokenValiditySeconds());
        // bcd.setResourceIds(resourceIds);
        return clientDetails
    }
}
