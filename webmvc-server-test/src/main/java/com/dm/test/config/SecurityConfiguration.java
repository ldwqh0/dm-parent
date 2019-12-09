package com.dm.test.config;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.dm.security.authentication.ResourceAuthorityService;
import com.dm.security.core.userdetails.GrantedAuthorityDto;
import com.dm.security.core.userdetails.UserDetailsDto;
import com.dm.security.web.authentication.AuthenticaionDecisionMaker;
import com.dm.security.web.authentication.DefaultAuthenticaionDecisionMaker;
import com.dm.security.web.authentication.LoginFailureHandler;
import com.dm.security.web.authentication.LoginSuccessHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private ObjectMapper om;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest()
                .access("@authenticaionDecisionMaker.check(authentication,request)")
                .and().formLogin();
        // 设置匿名用户的默认分组
        UserDetailsDto ud = new UserDetailsDto();
        List<GrantedAuthority> authorities = Collections
                .singletonList(new GrantedAuthorityDto("内置分组_ROLE_ANONYMOUS"));
        ud.setGrantedAuthority(authorities);
        http.anonymous().authorities(authorities).principal(ud);
        http.formLogin().successHandler(new LoginSuccessHandler(om)).failureHandler(new LoginFailureHandler(om));
        http.exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
        http.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
        http.csrf().disable();
    }

    @Bean
    public AuthenticaionDecisionMaker authenticaionDecisionMaker(ResourceAuthorityService authorityService) {
        DefaultAuthenticaionDecisionMaker dam = new DefaultAuthenticaionDecisionMaker();
        dam.setAuthorityService(authorityService);
        return dam;
    }
}
