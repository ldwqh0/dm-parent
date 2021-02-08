package com.dm.server.authorization.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import  com.dm.server.authorization.dto.ClientDto;
import  com.dm.server.authorization.entity.Client;
import org.xyyh.authorization.client.ClientDetailsService;


public interface ClientService extends ClientDetailsService {

    public Client save(ClientDto client);

    public boolean existAnyClient();

    public void delete(String clientId);

    public Optional<Client> findById(String id);

    public Client update(String id, ClientDto client);

    public Page<Client> find(String key, Pageable pageable);
}
