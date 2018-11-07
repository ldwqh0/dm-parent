package com.dm.auth.service.impl;

import java.util.Optional;
import java.util.UUID;

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
import com.dm.auth.service.ClientInfoService;
import com.dm.auth.service.UserApprovalService;

@Service("clientInfoService")
public class ClientInfoServiceImpl implements ClientInfoService, ClientDetailsService {

	@Autowired
	private ClientInfoRepository clientInfoRepository;

	@Autowired
	private ClientInfoConverter clientInfoConverter;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserApprovalService userApprovalService;

	@Override
	public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
		Optional<ClientInfo> client = clientInfoRepository.findById(clientId);
		return clientInfoConverter.toClientDetails(client);
	}

	@Transactional
	@Override
	public ClientInfo save(ClientInfoDto dto) {
		ClientInfo model = new ClientInfo();
		String id = UUID.randomUUID().toString();
		clientInfoConverter.copyProperties(model, dto);
		model.setClientSecret(passwordEncoder.encode(dto.getClientSecret()));
		model.setClientId(id);
		return clientInfoRepository.save(model);
	}

	@Override
	public boolean exist() {
		return clientInfoRepository.count() > 0;
	}

	@Override
	@Transactional
	public void delete(String clientId) {
		clientInfoRepository.deleteById(clientId);
		userApprovalService.deleteByClient(clientId);
	}

	@Override
	public Optional<ClientInfo> findById(String id) {
		return clientInfoRepository.findById(id);
	}

	@Override
	public Page<ClientInfo> find(String key, Pageable pageable) {
		return clientInfoRepository.findByNameContains(key, pageable);
	}

}
