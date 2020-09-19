package com.dm.security.oauth2.support.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.provider.ClientDetailsService;

import com.dm.security.oauth2.support.dto.ClientDto;
import com.dm.security.oauth2.support.entity.Client;

public interface ClientService extends ClientDetailsService {

    Client save(ClientDto app);

    boolean existAnyClient();

    void delete(String clientId);

    Optional<Client> findById(String id);

    Client update(String id, ClientDto client);

    Page<Client> find(String key, Pageable pageable);
}
