package com.dm.server.authorization.config

import com.dm.server.authorization.oauth2.ServerOpaqueTokenIntrospector
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher
import org.xyyh.oidc.core.OAuth2ResourceServerTokenService

/**
 * Copyright (C) 2019 江苏云智网络科技股份有限公司版权所有
 *
 * <p>服务器安全配置</p>
 *
 * @author 李东
 */
@EnableWebSecurity
class SecurityConfiguration(
    private val tokenService: OAuth2ResourceServerTokenService,
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http.authorizeRequests().antMatchers("/oauth2/styles/**", "/oauth2/static/**", "/error*").permitAll()
        http.authorizeRequests()
            .antMatchers(HttpMethod.GET, "/oauth2/logout").permitAll()
            .anyRequest().authenticated()
            .and().formLogin().loginPage("/oauth2/login.html")
            .loginProcessingUrl("/oauth2/login").permitAll()
            .and().httpBasic().disable()
        // 配置登出成功页面
        http.logout().logoutUrl("/oauth2/logout").logoutSuccessUrl("/")
        // 针对rest请求，返回401
        val mediaTypeRequestMatcher = MediaTypeRequestMatcher(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN)
        mediaTypeRequestMatcher.setIgnoredMediaTypes(setOf(MediaType.ALL))
        http.exceptionHandling()
            .defaultAuthenticationEntryPointFor(BearerTokenAuthenticationEntryPoint(), mediaTypeRequestMatcher)
        // 这里不处理匿名用户的问题
        // 正常情况下，所有带token的请求都会认为是resource请求，会返回401状态码
//        http.oauth2ResourceServer().opaqueToken().authenticationManager()
        http.oauth2ResourceServer().opaqueToken().introspector(opaqueTokenIntrospector())
    }

    @Bean
    fun opaqueTokenIntrospector(): ServerOpaqueTokenIntrospector {
        return ServerOpaqueTokenIntrospector(tokenService)
    }

    @Bean
    @Throws(Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

}
