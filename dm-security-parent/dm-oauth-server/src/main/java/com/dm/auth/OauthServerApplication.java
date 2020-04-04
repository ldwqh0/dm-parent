package com.dm.auth;

import java.time.ZonedDateTime;
import java.util.Optional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan
@EnableJpaRepositories
@ComponentScan
@SpringBootApplication
@EnableJpaAuditing(dateTimeProviderRef = "zonedDateTimeProvider")
@EnableCaching
public class OauthServerApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(OauthServerApplication.class, args);
    }

    @Bean(name = "zonedDateTimeProvider")
    public DateTimeProvider zonedDateTimeProvider() {
        return () -> Optional.of(ZonedDateTime.now());
    }
}
