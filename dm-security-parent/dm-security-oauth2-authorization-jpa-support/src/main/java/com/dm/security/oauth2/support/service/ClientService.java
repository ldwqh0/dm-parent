package com.dm.security.oauth2.support.service;

import org.springframework.security.oauth2.provider.ClientDetailsService;

import com.dm.security.oauth2.support.dto.ClientDto;
import com.dm.security.oauth2.support.entity.Client;


public interface ClientService extends ClientDetailsService {

    public Client save(ClientDto app);

    public boolean existAnyClient();
}
