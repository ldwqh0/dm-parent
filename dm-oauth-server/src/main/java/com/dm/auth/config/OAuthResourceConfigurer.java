package com.dm.auth.config;

import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@EnableResourceServer
@Order(99)
@EnableWebSecurity
public class OAuthResourceConfigurer extends ResourceServerConfigurerAdapter {
	@Override
	public void configure(HttpSecurity http) throws Exception {
		super.configure(http);
//		http.antMatcher("/oauth2/**");
	}

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		super.configure(resources);
		// 指定资源服务器的资源ID
		// 如果给某个授权指定了可也访问的资源ID，则仅能访问指定的资源，如果不给某个授权指定指定访问的资源ID，则代表可以访问所有资源
		resources.resourceId("USERS");
		resources.stateless(true); // 指定服务是无状态的
	}

}
