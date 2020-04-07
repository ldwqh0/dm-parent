package com.dm.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.web.server.session.CookieWebSessionIdResolver;
import org.springframework.web.server.session.DefaultWebSessionManager;
import org.springframework.web.server.session.WebSessionIdResolver;
import org.springframework.web.server.session.WebSessionManager;

@SpringBootApplication
@EnableCaching
public class GatewayApplication {

    @Value("${server.servlet.session.cookie.name:SESSION}")
    private String sessioncookiename;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(GatewayApplication.class, args);
    }

    /**
     * 重新配置webflux的session cookie name <br>
     * 
     * The bean name must be webSessionManager
     * 
     * @see <a href=
     *      "https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/web-reactive.html#webflux-web-handler-api-special-beans">Spring
     *      docs</a>
     * @return
     */
    @Bean
    public WebSessionManager webSessionManager() {
        DefaultWebSessionManager dwsm = new DefaultWebSessionManager();
        dwsm.setSessionIdResolver(webSessionIdResolver());
        return dwsm;
    }

    public WebSessionIdResolver webSessionIdResolver() {
        CookieWebSessionIdResolver resolver = new CookieWebSessionIdResolver();
        resolver.setCookieName(sessioncookiename);
        return resolver;
    }
}
