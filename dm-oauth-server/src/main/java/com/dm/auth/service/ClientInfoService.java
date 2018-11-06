package com.dm.auth.service;

import com.dm.auth.dto.ClientInfoDto;
import com.dm.auth.entity.ClientInfo;

public interface ClientInfoService {

	public boolean exist();

	public ClientInfo save(ClientInfoDto client);

	public void delete(String clientId);

}
