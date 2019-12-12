package com.dm.fileserver.config;

import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

public class Ac extends WebSecurityConfigurerAdapter {

//    @Autowired
//    private OAuth2ResourceServerProperties properties;
//
//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers(HttpMethod.GET).permitAll()
//                .anyRequest().authenticated();
//        http.oauth2ResourceServer().bearerTokenResolver(bearerTokenResolver)
//                .opaqueToken().introspector(opaqueTokenIntrospector());
//    }
//
//    @Bean
//    public OpaqueTokenIntrospector opaqueTokenIntrospector() {
//        return new UserInfoOpaqueTokenIntrospector(properties.getOpaquetoken().getIntrospectionUri());
//    }

}