package com.dm.security.oauth2.support.service.impl;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        Client model = Optional.ofNullable(app.getId())
                .map(Client::new)
                .orElse(new Client());
        return clientRepository.save(clientConverter.copyProperties(model, app));
    }

    @Override
    @Transactional(readOnly = true)
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        return clientRepository.findById(clientId)
                .map(clientConverter::toClientDetails)
                .orElseThrow(() -> new ClientRegistrationException("the client " + clientId + "is not exists"));
    }

    @Override
    public boolean existAnyClient() {
        return clientRepository.count() > 0;
    }

    @Override
    @Transactional
    public void delete(String clientId) {
        clientRepository.deleteById(clientId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Client> findById(String id) {
        return clientRepository.findById(id);
    }

    @Override
    @Transactional
    public Client update(String id, ClientDto client) {
        Client c_ = clientRepository.getOne(id);
        c_ = clientConverter.copyProperties(c_, client);
        return clientRepository.save(c_);
    }

    @Override
    public Page<Client> find(String key, Pageable pageable) {
        // TODO 需要增加查询参数
        return clientRepository.findAll(pageable);
    }

}
