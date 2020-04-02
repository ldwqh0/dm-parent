package com.dm.security.oauth2.introspection;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

/**
 * 从授权服务器直接读取token信息的实现
 * 
 * @author LiDong
 *
 */
public class AuthorizationTokenIntrospector implements OpaqueTokenIntrospector {

    @Autowired
    private ResourceServerTokenServices tokenService;

    private Function<OAuth2Authentication, OAuth2AuthenticatedPrincipal> principalConverter;

    public void setPrincipalConverter(Function<OAuth2Authentication, OAuth2AuthenticatedPrincipal> principalConverter) {
        this.principalConverter = principalConverter;
    }

    public void setTokenService(ResourceServerTokenServices tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        OAuth2Authentication au = tokenService.loadAuthentication(token);
        Object principal = au.getPrincipal();
        if (!Objects.isNull(principalConverter)) {
            return principalConverter.apply(au);
        } else if (!Objects.isNull(principal) &&
                (principal instanceof OAuth2AuthenticatedPrincipal)) {
            return (OAuth2AuthenticatedPrincipal) principal;
        } else {
            Collection<GrantedAuthority> authorities = au.getAuthorities();
            String name = au.getName();
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("sub", name);
            attributes.put("principal", au.getPrincipal());
            return new DefaultOAuth2AuthenticatedPrincipal(name, attributes, authorities);
        }

    }

}
