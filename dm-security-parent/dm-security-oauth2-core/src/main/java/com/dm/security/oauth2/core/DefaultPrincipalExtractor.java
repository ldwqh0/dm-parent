package com.dm.security.oauth2.core;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;

public class DefaultPrincipalExtractor implements PrincipalExtractor {

    private String userNameAttributeName = "username";

    public void setUserNameAttributeName(String userNameAttributeName) {
        this.userNameAttributeName = userNameAttributeName;
    }

    @Override
    public OAuth2User extract(Map<String, Object> attrs) {
        GrantedAuthority authority = new OAuth2UserAuthority(attrs);
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(authority);

        // 源自 DefaultReactiveOAuth2UserService
//        OAuth2AccessToken token = userRequest.getAccessToken();
//        for (String scope : token.getScopes()) {
//            authorities.add(new SimpleGrantedAuthority("SCOPE_" + scope));
//        }

        return new DefaultOAuth2User(authorities, attrs, userNameAttributeName);
    }

}
