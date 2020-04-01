package com.dm.security.oauth2.support.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dm.security.oauth2.support.converter.ClientConverter;
import com.dm.security.oauth2.support.dto.ClientDto;
import com.dm.security.oauth2.support.entity.Client;
import com.dm.security.oauth2.support.repository.ClientRepository;
import com.dm.security.oauth2.support.service.ClientService;


@Service
public class ClientServiceImpl implements ClientService, ClientDetailsService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientConverter clientConverter;

    @Override
    @Transactional
    public Client save(ClientDto app) {
        return clientRepository.save(clientConverter.copyProperties(new Client(), app));
    }

    @Override
    @Transactional(readOnly = true)
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        return clientRepository.findById(UUID.fromString(clientId))
                .map(clientConverter::toClientDetails)
                .orElseThrow(() -> new ClientRegistrationException("the client " + clientId + "is not exists"));
    }

    @Override
    public boolean existAnyClient() {
        return clientRepository.count() > 0;
    }

}
