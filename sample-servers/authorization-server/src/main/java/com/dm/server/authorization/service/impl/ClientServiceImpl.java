package com.dm.server.authorization.service.impl;

import com.dm.server.authorization.converter.ClientConverter;
import com.dm.server.authorization.dto.ClientDto;
import com.dm.server.authorization.entity.Client;
import com.dm.server.authorization.repository.ClientRepository;
import com.dm.server.authorization.service.ClientService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xyyh.authorization.client.ClientDetails;
import org.xyyh.authorization.client.ClientDetailsService;
import org.xyyh.authorization.exception.NoSuchClientException;

import java.util.Optional;

/**
 * <p>连接管理服务</p>
 *
 * @author ldwqh0@outlook.com
 */
@Service
public class ClientServiceImpl implements ClientService, ClientDetailsService {

    private final ClientRepository clientRepository;

    private final ClientConverter clientConverter;

    public ClientServiceImpl(ClientRepository clientRepository, ClientConverter clientConverter) {
        this.clientRepository = clientRepository;
        this.clientConverter = clientConverter;
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "clients", key = "#result.id")
    public ClientDto save(ClientDto app) {
        Client model = Optional.ofNullable(app.getId())
            .map(Client::new)
            .orElseGet(Client::new);
        return clientConverter.toDto(clientRepository.save(clientConverter.copyProperties(model, app)));
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "clients", sync = true)
    public ClientDetails loadClientByClientId(String clientId) throws NoSuchClientException {
        return clientRepository.findById(clientId)
            .map(clientConverter::toClientDetails)
            .orElseThrow(() -> new NoSuchClientException("the client " + clientId + "is not exists"));
    }

    @Override
    public boolean existAnyClient() {
        return clientRepository.count() > 0;
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "clients")
    public void delete(String clientId) {
        clientRepository.deleteById(clientId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClientDto> findById(String id) {
        return clientRepository.findById(id).map(clientConverter::toDto);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "clients", key = "#id")
    public ClientDto update(String id, ClientDto client) {
        Client c_ = clientRepository.getOne(id);
        c_ = clientConverter.copyProperties(c_, client);
        return clientConverter.toDto(clientRepository.save(c_));
    }

    @Override
    public Page<ClientDto> find(String key, Pageable pageable) {
        // TODO 需要增加查询参数
        return clientRepository.findAll(pageable).map(clientConverter::toDto);
    }

}
