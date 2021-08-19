package com.dm.server.authorization.oauth2

//class ServerOpaqueTokenIntrospector(
//    private val accessTokenService: OAuth2ResourceServerTokenService
//) : OpaqueTokenIntrospector {
//    override fun introspect(token: String): OAuth2AuthenticatedPrincipal {
//        Assert.notNull(accessTokenService, "the accessTokenService can not be null")
//        val storedToken = accessTokenService.readAccessToken(token)
//            .orElseThrow { BadOpaqueTokenException("The token [$token] is not exist") }
//        val authentication = accessTokenService.loadAuthentication(token)
//            .orElseThrow { BadOpaqueTokenException("The authentication with token [$token] is not exist") }
//        authentication.user?.claims?.set(OAuth2IntrospectionClaimNames.ISSUED_AT, storedToken.issuedAt)
//        authentication.user?.claims?.set(OAuth2IntrospectionClaimNames.EXPIRES_AT, storedToken.expiresAt)
//
//
//        return authentication.user
//            ?: throw BadOpaqueTokenException("the authentication with token [$token] has no userinfo")
//    }
//}
