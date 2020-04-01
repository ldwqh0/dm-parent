package com.dm.springboot.autoconfigure.oauth2;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ConditionalOnClass(name = { "com.dm.security.oauth2.support.entity.Client" })

@EntityScan({ "com.dm.security.oauth2.support" })
@EnableJpaRepositories({ "com.dm.security.oauth2.support" })
@ComponentScan({ "com.dm.security.oauth2.support" })
public class Oauth2JpaSupportConfiguration {

}
