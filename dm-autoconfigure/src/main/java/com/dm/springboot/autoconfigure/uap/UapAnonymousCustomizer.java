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

    private UserService userService = null;

    public UapAnonymousCustomizer() {

    }

    @Autowired
    public void userDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setUapAutoConfiguration(UapAutoConfiguration uac) {
        /**
         * 这个没有什么用，仅仅表示本类需要 {@link UapAutoConfiguration}加载并执行完成之后才能执行
         */
    }

    @Override
    public void customize(AnonymousConfigurer<HttpSecurity> http) {
        UserDetails anonymousDetails = userService.loadUserByUsername("ANONYMOUS");
        List<GrantedAuthority> authorities = new ArrayList<>(anonymousDetails.getAuthorities());
        http.authorities(authorities).principal(anonymousDetails);
    }
}
