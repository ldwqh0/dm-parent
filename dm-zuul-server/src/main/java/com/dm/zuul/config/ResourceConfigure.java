package com.dm.zuul.config;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.dm.security.oauth2.access.AuthorityChecker;
import com.dm.security.oauth2.access.RequestAuthorityService;

/**
 * 当请求中携带了access_token之后， 进行oauth认证
 * 
 * @author LiDong
 *
 */
@EnableResourceServer
@EnableWebSecurity
@Order(99)
public class ResourceConfigure extends ResourceServerConfigurerAdapter {

    /**
     * 配置对资源的保护模式
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        // 指定所有的资源都要被保护
        http.authorizeRequests().anyRequest().access("@authorityChecker.check(authentication,request)");
        // 仅仅将携带了token的资源，定义为资源服务器的资源，走oauth认证
        // ，其它的资源都走普通的session认证
        http.requestMatcher(new BearerTokenRequestMatcher());
        // TODO 需要重新配置匿名认证的模式，匿名认证的时候，返回一个默认的匿名用户？
        // 资源服务器要如何处理匿名访问？是单独配置？还是通过token的方式传递用户信息到上游服务器
        // 决定 ，网关暂时不做匿名的处理
        // 暂时不做匿名处理
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        super.configure(resources);
        // 重新配置ExpressionHandler
        // 默认的OAuth2WebSecurityExpressionHandler是在ResourceServerSecurityConfigurer中实例化的。
        // 没有applicationContext,不能解析需要bean对象的表达式
        resources.expressionHandler(expressionHandler());
    }

    static class BearerTokenRequestMatcher implements RequestMatcher {
        private boolean matchHeader(HttpServletRequest request) {
            String authHeader = request.getHeader("Authorization");
            return StringUtils.startsWithIgnoreCase(authHeader, OAuth2AccessToken.BEARER_TYPE);
        }

        @Override
        public boolean matches(HttpServletRequest request) {
            return matchHeader(request) || matchParameter(request);
        }

        private boolean matchParameter(HttpServletRequest request) {
            return StringUtils.isNotBlank(request.getParameter(OAuth2AccessToken.ACCESS_TOKEN));
        }
    }

    /**
     * 构造一个新的OAuth2WebSecurityExpressionHandler类的bean <br>
     * 构造的目的是为了给它设置applicationContext<br>
     * OAuth2WebSecurityExpressionHandler实现了{@link ApplicationContextAware}接口，会主动注入
     * 
     * @return
     */
    @Bean
    public OAuth2WebSecurityExpressionHandler expressionHandler() {
        return new OAuth2WebSecurityExpressionHandler();
    }

    /**
     * 权限校验器
     * 
     * @param authorityService
     * @return
     */
    @Bean
    public AuthorityChecker authorityChecker(RequestAuthorityService authorityService) {
        AuthorityChecker authorityChecker = new AuthorityChecker();
        authorityChecker.setAuthorityService(authorityService);
        return authorityChecker;
    }

}
