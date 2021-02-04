package com.dm.server.authorization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.xyyh.authorization.configuration.annotation.EnableAuthorizationServer;

import java.time.ZonedDateTime;
import java.util.Optional;

@EnableAuthorizationServer
@SpringBootApplication
@EnableJpaRepositories
@EntityScan
@EnableJpaAuditing(dateTimeProviderRef = "zonedDateTimeProvider")
public class AuthorizationApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthorizationApplication.class, args);
    }

    @Bean(name = "zonedDateTimeProvider")
    public DateTimeProvider zonedDateTimeProvider() {
        return () -> Optional.of(ZonedDateTime.now());
    }
}
