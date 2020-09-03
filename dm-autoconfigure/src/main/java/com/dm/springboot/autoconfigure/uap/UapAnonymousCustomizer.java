package com.dm.springboot.autoconfigure.uap;

import com.dm.uap.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AnonymousConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

public class UapAnonymousCustomizer implements Customizer<AnonymousConfigurer<HttpSecurity>> {

    /**
     * 这个没有什么用，仅仅表示本类需要 {@link UapAutoConfiguration}加载并执行完成之后才能执行
     */
    @SuppressWarnings("unused")
    private UapAutoConfiguration uac;

    private UserService userService;

    public UapAnonymousCustomizer() {

    }

    @Autowired
    public void userDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setUapAutoConfiguration(UapAutoConfiguration uac) {
        this.uac = uac;
    }

    @Override
    public void customize(AnonymousConfigurer<HttpSecurity> http) {
        UserDetails anonymousDetails = userService.loadUserByUsername("ANONYMOUS");
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.addAll(anonymousDetails.getAuthorities());
        http.authorities(authorities).principal(anonymousDetails);
    }
}