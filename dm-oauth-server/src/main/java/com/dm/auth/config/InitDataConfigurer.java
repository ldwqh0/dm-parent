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

	/**
	 * 系统启动时，新建一个默认应用
	 */
	private void init() {
		ClientInfoDto client = new ClientInfoDto();
		client.setClientId("ownerapp");
		client.setAccessTokenValiditySeconds(60000);
		client.setAuthorizedGrantTypes(Collections.singleton("password"));
		client.setClientSecret("123456");
		client.setName("自有应用");
		client.setRefreshTokenValiditySeconds(60000);
		client.setScope(Collections.singleton("app"));
		clientService.save(client);
	}
}
