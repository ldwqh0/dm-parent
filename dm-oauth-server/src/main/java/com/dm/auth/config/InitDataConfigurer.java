package com.dm.auth.config;

import java.util.Collections;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.dm.auth.dto.ClientInfoDto;
import com.dm.auth.service.ClientInfoService;

@Configuration
public class InitDataConfigurer {

	@Autowired
	private ClientInfoService clientService;

	@PostConstruct
	public void initData() {
		if (!clientService.exist()) {
			init();
		}
	}

	private void init() {
		ClientInfoDto client = new ClientInfoDto();
		client.setAccessTokenValiditySeconds(60000);
		client.setAuthorizedGrantTypes(Collections.singleton("authorization_code"));
		client.setClientSecret("123456");
		client.setRefreshTokenValiditySeconds(50000);
		client.setRegisteredRedirectUri(Collections.singleton("http://www.baidu.com"));
		client.setScope(Collections.singleton("app"));
		clientService.save(client);
	}
}
