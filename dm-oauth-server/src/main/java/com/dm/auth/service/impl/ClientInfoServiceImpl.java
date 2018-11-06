package com.dm.auth.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

import com.dm.auth.converter.ClientInfoConverter;
import com.dm.auth.entity.ClientInfo;
import com.dm.auth.repository.ClientInfoRepository;
import com.dm.auth.service.ClientInfoService;

@Service("clientInfoService")
public class ClientInfoServiceImpl implements ClientInfoService, ClientDetailsService {

	@Autowired
	private ClientInfoRepository clientInfoRepository;

	@Autowired
	private ClientInfoConverter clientInfoConverter;

	@Override
	public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
		Optional<ClientInfo> client = clientInfoRepository.findById(clientId);
		return clientInfoConverter.toClientDetails(client);
	}

}
