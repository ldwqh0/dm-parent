package com.dm.auth.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dm.auth.dto.ClientInfoDto;
import com.dm.auth.entity.ClientInfo;

public interface ClientInfoService {

	public boolean exist();

	public ClientInfo save(ClientInfoDto client);

	public void delete(String clientId);

	public Optional<ClientInfo> findById(String id);

	public Page<ClientInfo> find(String key, Pageable pageable);

}
