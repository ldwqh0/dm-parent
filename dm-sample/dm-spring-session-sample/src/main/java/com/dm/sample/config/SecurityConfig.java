package com.dm.sample.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.dm.security.web.authentication.LoginFailureHandler;
import com.dm.security.web.authentication.LoginSuccessHandler;
import com.dm.uap.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        LoginSuccessHandler loginSuccessHandler = new LoginSuccessHandler(objectMapper);
        LoginFailureHandler LoginFailureHandler = new LoginFailureHandler(objectMapper);

        http.authorizeRequests()
                .anyRequest().authenticated();
        http.formLogin().successHandler(loginSuccessHandler).failureHandler(LoginFailureHandler);

//        http.exceptionHandling().defaultAuthenticationEntryPointFor(entryPoint, preferredMatcher)
//        http.exceptionHandling().authenticationEntryPoint();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

}
