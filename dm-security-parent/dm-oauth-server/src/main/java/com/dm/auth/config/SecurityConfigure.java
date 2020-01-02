package com.dm.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.dm.auth.security.SavedRequestAwareAuthenticationAndLoggingSuccessHandler;
import com.dm.auth.security.SimpleUrlAuthenticationLoginLogFailureHandler;
import com.dm.uap.service.LoginLogService;

@Configuration
@EnableWebSecurity
public class SecurityConfigure extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userService;

    @Value(value = "${spring.security.default-success-url:/oauth/index.html}")
    private String defaultSuccessUrl;

    @Autowired
    private LoginLogService loginLogService;

    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // 如果不使用默认的failureHandler，需要手动指定登录失败页面，也就是login.html?error的权限为跳过
                // 默认的跳过页面由 AbstractAuthenticationFilterConfigurer.updateAccessDefaults 设置跳过页面
                // 设置逻辑可以参考相应源码
                .antMatchers("/oauth/login.html**", "/oauth/styles/**", "/oauth/images/**", "/favicon.ico").permitAll()
                .anyRequest().authenticated().and().formLogin().loginPage("/oauth/login.html")
                .loginProcessingUrl("/oauth/login").permitAll().failureHandler(authenticationFailureHandler())
                .successHandler(authenticationSuccessHandler()) // .defaultSuccessUrl(defaultSuccessUrl)
                .and().httpBasic().disable();
//        http.logout().logoutUrl("/oauth/logout");
    }

    private AuthenticationFailureHandler authenticationFailureHandler() {
        SimpleUrlAuthenticationLoginLogFailureHandler failureHandler = new SimpleUrlAuthenticationLoginLogFailureHandler();
        failureHandler.setDefaultFailureUrl("/oauth/login.html?error");
        failureHandler.setLoginLogService(loginLogService);
        return failureHandler;
    }

    private AuthenticationSuccessHandler authenticationSuccessHandler() {
        SavedRequestAwareAuthenticationAndLoggingSuccessHandler handler = new SavedRequestAwareAuthenticationAndLoggingSuccessHandler();
        handler.setDefaultTargetUrl(defaultSuccessUrl);
        handler.setAlwaysUseDefaultTargetUrl(false);
        handler.setLoginLogService(loginLogService);
        return handler;
    }

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
        auth.userDetailsService(userService);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
