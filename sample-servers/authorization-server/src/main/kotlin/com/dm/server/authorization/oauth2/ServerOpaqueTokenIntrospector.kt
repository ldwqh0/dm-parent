package com.dm.server.authorization.oauth2

import com.dm.security.core.userdetails.UserDetailsDto
import com.dm.security.oauth2.core.OAuth2UserDetailsDto
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal
import org.springframework.security.oauth2.server.resource.introspection.BadOpaqueTokenException
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionClaimNames
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector
import org.springframework.util.Assert
import org.xyyh.authorization.core.OAuth2ResourceServerTokenService
import java.util.*


class ServerOpaqueTokenIntrospector(
    private val accessTokenService: OAuth2ResourceServerTokenService
) : OpaqueTokenIntrospector {
    override fun introspect(token: String): OAuth2AuthenticatedPrincipal {
        val result = OAuth2UserDetailsDto()
        Assert.notNull(accessTokenService, "the accessTokenService can not be null")
        val storedToken = accessTokenService.readAccessToken(token)
            .orElseThrow { BadOpaqueTokenException("The token [$token] is not exist") }
        val authentication = accessTokenService.loadAuthentication(token)
            .orElseThrow { BadOpaqueTokenException("The authentication with token [$token] is not exist") }
        val attributes: MutableMap<String, Any> = HashMap()
        val expiresAt = storedToken.expiresAt
        val issuedAt = storedToken.issuedAt
        val scopes = storedToken.scopes
        val tokenType = storedToken.tokenType
        attributes[OAuth2IntrospectionClaimNames.ACTIVE] = true
        attributes[OAuth2IntrospectionClaimNames.EXPIRES_AT] = expiresAt
        attributes[OAuth2IntrospectionClaimNames.ISSUED_AT] = issuedAt
        attributes[OAuth2IntrospectionClaimNames.SCOPE] = scopes
        attributes[OAuth2IntrospectionClaimNames.TOKEN_TYPE] = tokenType
        attributes[OAuth2IntrospectionClaimNames.CLIENT_ID] = Objects.requireNonNull(authentication).client.clientId
        attributes[OAuth2IntrospectionClaimNames.NOT_BEFORE] = issuedAt
        result.clientId = authentication.client.clientId
        result.scopes = authentication.scopes
        val principal = authentication.principal
        if (principal is UserDetails) {
            val username = principal.username
            result.username = username
            attributes[OAuth2IntrospectionClaimNames.USERNAME] = username
            result.setGrantedAuthority(principal.authorities)
        }
        if (principal is UserDetailsDto) {
            val userid = principal.id
            attributes[OAuth2IntrospectionClaimNames.SUBJECT] = userid
            result.id = userid
        }
        result.attributes = Collections.unmodifiableMap(attributes)
        return result
    }
}
