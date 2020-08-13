package com.dm.springboot.autoconfigure.security;

import java.util.Collections;
import java.util.List;

import javax.servlet.Servlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.dm.security.core.userdetails.GrantedAuthorityDto;
import com.dm.security.core.userdetails.UserDetailsDto;
import com.dm.security.web.authentication.LoginFailureHandler;
import com.dm.security.web.authentication.LoginSuccessHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

@Deprecated
@ConditionalOnClass({ Servlet.class, WebSecurityConfigurerAdapter.class })
@ConditionalOnMissingBean({ WebSecurityConfigurerAdapter.class, SecurityWebFilterChain.class })
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private ObjectMapper om;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().authenticated().and().formLogin();
        // 设置匿名用户的默认分组
        UserDetailsDto ud = new UserDetailsDto();
        List<GrantedAuthority> authorities = Collections.singletonList(new GrantedAuthorityDto("内置分组_ROLE_ANONYMOUS"));
        ud.setGrantedAuthority(authorities);
        http.anonymous().authorities(authorities).principal(ud);
        http.formLogin().successHandler(new LoginSuccessHandler(om)).failureHandler(new LoginFailureHandler(om));
        http.exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
        http.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
    }
}
