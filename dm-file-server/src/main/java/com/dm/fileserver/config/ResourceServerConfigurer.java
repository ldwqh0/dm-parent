package com.dm.fileserver.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

import com.dm.security.oauth2.resource.UserDetailsDtoPrincipalExtractor;

@EnableResourceServer
@EnableWebSecurity
public class ResourceServerConfigurer extends ResourceServerConfigurerAdapter {
	@Autowired
	private UserInfoTokenServices userInfoTokenServices;

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers(HttpMethod.GET).anonymous();
		http.authorizeRequests().anyRequest().authenticated();
		http.antMatcher("/**");
	}

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		super.configure(resources);
		// 指定这是一个restful service,不会保存会话状态
		resources.stateless(true);
	}

	@PostConstruct
	public void config() {
		userInfoTokenServices.setPrincipalExtractor(principalExtractor());
	}

	/**
	 * 指定一个用户信息解码器，将从服务器获取过来的用户信息解码为本地UserDetails
	 * 
	 * @return
	 */
	@Bean
	public PrincipalExtractor principalExtractor() {
		return new UserDetailsDtoPrincipalExtractor();
	}
}
