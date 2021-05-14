package com.dm.server.authorization.converter

import com.dm.security.core.userdetails.UserDetailsDto
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionClaimNames.SUBJECT
import org.springframework.stereotype.Component
import org.xyyh.authorization.core.OAuth2Authentication
import org.xyyh.authorization.core.OAuth2ServerAccessToken
import org.xyyh.authorization.endpoint.converter.DefaultAccessTokenConverter


@Component
class DmAccessTokenConverter : DefaultAccessTokenConverter() {
    override fun toAccessTokenIntrospectionResponse(
        token: OAuth2ServerAccessToken?,
        authentication: OAuth2Authentication
    ): Map<String, Any> {
        val details = authentication.principal
        val response = super.toAccessTokenIntrospectionResponse(token, authentication)
        response["authorities"] = authentication.authorities
        when (details) {
            is String -> {
                response[SUBJECT] = details
            }
            is UserDetailsDto -> {
                details.id?.let {
                    response[SUBJECT] = it.toString()
                }
                // 添加用户名
                response["fullname"] = details.fullname
            }
            is UserDetails -> {
                response[SUBJECT] = details.username
            }
        }
        return response
    }
}
