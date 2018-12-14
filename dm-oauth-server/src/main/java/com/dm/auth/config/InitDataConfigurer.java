package com.dm.auth.config;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.dm.auth.dto.ClientInfoDto;
import com.dm.auth.entity.ClientInfo;
import com.dm.auth.service.ClientInfoService;

@Configuration
public class InitDataConfigurer {

	@Autowired
	private ClientInfoService clientService;

	@PostConstruct
	public void initData() {
		Optional<ClientInfo> ownerapp = clientService.getOne("ownerapp");
		Optional<ClientInfo> zuul = clientService.getOne("zuul");
		if (!ownerapp.isPresent()) {
			initOwnerApp();
		}
		if (!zuul.isPresent()) {
			initZuul();
		}
	}

	/**
	 * 系统启动时，新建一个默认应用
	 */
	private void initOwnerApp() {
		ClientInfoDto client = new ClientInfoDto();
		client.setClientId("ownerapp");
		client.setAccessTokenValiditySeconds(60000);
		Set<String> grantTypes = new HashSet<String>();
		grantTypes.add("password"); // 初始化的app有默认的两种grandType
		grantTypes.add("refresh_token");
		client.setAuthorizedGrantTypes(grantTypes);
		client.setClientSecret("123456");
		client.setName("自有应用");
		client.setRefreshTokenValiditySeconds(60000);
		client.setScope(Collections.singleton("app"));
		clientService.save(client);
	}

	private void initZuul() {
		ClientInfoDto zuul = new ClientInfoDto();
		zuul.setClientId("zuul");
		zuul.setClientSecret("123456");
		zuul.setAccessTokenValiditySeconds(6000);
		zuul.setRefreshTokenValiditySeconds(6000);
		Set<String> grantTypes = new HashSet<String>();
		grantTypes.add("authorization_code"); // 初始化的zuul有默认的一种grantType
		zuul.setAuthorizedGrantTypes(grantTypes);
		zuul.setName("应用网关");
		zuul.setScope(Collections.singleton("app"));
		clientService.save(zuul);
	}
}
