package com.dm.server.authorization.service

import org.xyyh.oidc.core.OAuth2ResourceServerTokenService
import org.xyyh.oidc.core.OidcAuthentication

fun OAuth2ResourceServerTokenService.loadAuthenticationOrNull(token: String): OidcAuthentication? =
    loadAuthentication(token).orElse(null)

fun OAuth2ResourceServerTokenService.readAccessTokenOrNull(token: String): Any = readAccessToken(token).orElse(null)
