package com.dm.server.authorization.config

import com.dm.server.authorization.service.loadAuthenticationOrNull
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken
import org.xyyh.oidc.core.OAuth2ResourceServerTokenService

class ServerOpaqueTokenAuthenticationManager(
    private val tokenService: OAuth2ResourceServerTokenService
) : AuthenticationManager {

    override fun authenticate(authentication: Authentication): Authentication? {
        return if (authentication is BearerTokenAuthenticationToken) {
            this.tokenService.loadAuthenticationOrNull(authentication.token);
        } else {
            null
        }
    }
}
