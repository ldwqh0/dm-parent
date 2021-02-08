package com.dm.server.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.MapSession;
import org.springframework.session.ReactiveMapSessionRepository;
import org.springframework.session.ReactiveSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.config.annotation.web.server.EnableSpringWebSession;
import org.springframework.web.server.session.CookieWebSessionIdResolver;
import org.springframework.web.server.session.WebSessionIdResolver;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Session配置<br>
 *
 * 根据实际情况启用不同的WebSession<br>
 * 在单机环境下，使用 {@link EnableSpringWebSession}<br>
 * 根据实际情况确定是否使用其它的Session存储<br>
 * 具体的可以参考<a href="https://spring.io/projects/spring-session">Spring session</a>
 *
 *
 * @author LiDong
 *
 */
@Configuration
@EnableSpringWebSession
public class SessionConfiguration {

    @Bean
    public WebSessionIdResolver webSessionIdResolver() {
        CookieWebSessionIdResolver resolver = new CookieWebSessionIdResolver();
        resolver.setCookieName("GATEWAYSESSIONID");
        return resolver;
    }

    @Bean
    public ReactiveSessionRepository<MapSession> sessionRepository() {
        Map<String, Session> store = Collections.synchronizedMap(new HashMap<String, Session>());
        return new ReactiveMapSessionRepository(store);
    }
}
