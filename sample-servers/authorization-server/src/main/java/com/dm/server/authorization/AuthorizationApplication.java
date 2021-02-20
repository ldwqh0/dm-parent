package com.dm.server.authorization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.xyyh.authorization.configuration.annotation.EnableAuthorizationServer;

import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * <p>授权服务器入口</p>
 *
 * @author ldwqh0@outlook.com
 */
@EnableAuthorizationServer
@SpringBootApplication
@EnableJpaRepositories
@EntityScan
@EnableJpaAuditing(dateTimeProviderRef = "zonedDateTimeProvider")
@EnableCaching
public class AuthorizationApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthorizationApplication.class, args);
    }

    /**
     * 项目中使用zonedDateTimeProvider作为时间获取器
     *
     * @return a {@link DateTimeProvider}
     */
    @Bean(name = "zonedDateTimeProvider")
    public DateTimeProvider zonedDateTimeProvider() {
        return () -> Optional.of(ZonedDateTime.now());
    }
}
