package com.dm.auth.config;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.dm.security.oauth2.support.dto.ClientDto;
import com.dm.security.oauth2.support.entity.Client;
import com.dm.security.oauth2.support.service.ClientService;


@Configuration
public class InitDataConfigurer {

    @Autowired
    private ClientService clientService;

    @PostConstruct
    public void initData() {
        Optional<Client> ownerapp = clientService.findById("ownerapp");
        Optional<Client> zuul = clientService.findById("zuul");
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
        ClientDto client = new ClientDto();
        client.setId("ownerapp");
        client.setAccessTokenValiditySeconds(60000);
        Set<String> grantTypes = new HashSet<String>();
        grantTypes.add("password"); // 初始化的app有默认的两种grandType
        grantTypes.add("refresh_token");
        client.setAuthorizedGrantTypes(grantTypes);
        client.setSecret("123456");
        client.setName("自有应用");
        client.setRefreshTokenValiditySeconds(60000);
        client.setScope(Collections.singleton("app"));
        clientService.save(client);
    }

    private void initZuul() {
        ClientDto zuul = new ClientDto();
        zuul.setId("zuul");
        zuul.setSecret("123456");
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
