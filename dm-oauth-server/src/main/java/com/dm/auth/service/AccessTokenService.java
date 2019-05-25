package com.dm.auth.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dm.auth.dto.AccessTokenInfoDto;

public interface AccessTokenService {

	public Page<AccessTokenInfoDto> listTokensByClient(String client, Pageable pageable);

}
