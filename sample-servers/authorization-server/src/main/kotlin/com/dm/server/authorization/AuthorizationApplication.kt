package com.dm.server.authorization

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.data.auditing.DateTimeProvider
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.xyyh.oidc.configuration.annotation.EnableAuthorizationServer
import java.time.ZonedDateTime
import java.util.*

/**
 * <p>Copyright (C) 2019 江苏云智网络科技股份有限公司版权所有</p>
 * <p>授权服务器入口</p>
 *
 * @author LiDong
 */
@EnableAuthorizationServer
@SpringBootApplication
@EnableJpaRepositories
@EntityScan
@EnableJpaAuditing(dateTimeProviderRef = "zonedDateTimeProvider")
//@EnableCaching
class AuthorizationApplication {
    @Bean(name = ["zonedDateTimeProvider"])
    fun zonedDateTimeProvider(): DateTimeProvider {
        return DateTimeProvider { Optional.of(ZonedDateTime.now()) }
    }
}

fun main(args: Array<String>) {
    runApplication<AuthorizationApplication>(*args)
}



