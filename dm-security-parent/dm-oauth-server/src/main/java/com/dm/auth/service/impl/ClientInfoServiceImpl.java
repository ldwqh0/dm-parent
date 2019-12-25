package com.dm.auth.service.impl;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dm.auth.converter.ClientInfoConverter;
import com.dm.auth.dto.ClientInfoDto;
import com.dm.auth.entity.ClientInfo;
import com.dm.auth.repository.ClientInfoRepository;
import com.dm.auth.repository.UserApprovalRepository;
import com.dm.auth.service.ClientInfoService;

@Service("clientInfoService")
public class ClientInfoServiceImpl implements ClientInfoService, ClientDetailsService {

    @Autowired
    private ClientInfoRepository clientInfoRepository;

    @Autowired
    private ClientInfoConverter clientInfoConverter;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserApprovalRepository userApprovalRepository;

    @Override
    public ClientDetails loadClientByClientId(final String clientId) throws ClientRegistrationException {
        if (StringUtils.isBlank(clientId)) {
            throw new ClientRegistrationException("clientid can not be null!");
        }
        Optional<ClientInfo> client = clientInfoRepository.findById(clientId);
        return client.flatMap(clientInfoConverter::toClientDetails)
                .orElseThrow(
                        () -> new ClientRegistrationException("The client with id {" + clientId + "} is not exists"));
    }

    @Transactional
    @Override
    public ClientInfo save(ClientInfoDto dto) {
        ClientInfo model = new ClientInfo();
        String id;
        if (Objects.isNull(dto.getClientId())) {
            id = UUID.randomUUID().toString();
        } else {
            id = dto.getClientId();
        }
        clientInfoConverter.copyProperties(model, dto);
        model.setClientSecret(passwordEncoder.encode(dto.getClientSecret()));
        model.setClientId(id);
        return clientInfoRepository.save(model);
    }

    @Override
    public boolean exist() {
        return clientInfoRepository.count() > 0;
    }

    /**
     * 删除一个client
     */
    @Override
    @Transactional
    public void delete(String clientId) {
        userApprovalRepository.deleteByClientId(clientId);
        clientInfoRepository.deleteById(clientId);
    }

    @Override
    public Optional<ClientInfo> findById(String id) {
        return clientInfoRepository.findById(id);
    }

    @Override
    public Page<ClientInfo> find(String key, Pageable pageable) {
        if (StringUtils.isNotBlank(key)) {
            return clientInfoRepository.find(key, pageable);
        } else {
            return clientInfoRepository.findAll(pageable);
        }
    }

    @Override
    @Transactional
    public ClientInfo update(String id, ClientInfoDto client) {
        ClientInfo _client = clientInfoRepository.getOne(id);
        clientInfoConverter.copyProperties(_client, client);
        _client.setClientSecret(passwordEncoder.encode(client.getClientSecret()));
        return _client;
    }

}
