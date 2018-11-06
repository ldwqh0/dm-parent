package com.dm.auth.converter;

import java.util.Optional;

import org.springframework.security.oauth2.provider.ClientDetails;

import com.dm.auth.dto.ClientDetailsDto;
import com.dm.auth.dto.ClientInfoDto;
import com.dm.auth.entity.ClientInfo;
import com.dm.common.converter.AbstractConverter;

public class ClientInfoConverter extends AbstractConverter<ClientInfo, ClientInfoDto> {

	@Override
	protected ClientInfoDto toDtoActual(ClientInfo model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void copyProperties(ClientInfo model, ClientInfoDto dto) {
		// TODO Auto-generated method stub

	}

	public ClientDetailsDto toClientDetails(ClientInfo model) {
		if (model == null) {
			return null;
		} else {
			return toClientDetailsActual(model);
		}
	}

	public ClientDetails toClientDetails(Optional<ClientInfo> client) {
		if (client.isPresent()) {
			return toClientDetailsActual(client.get());
		} else {
			return null;
		}

	}

	private ClientDetailsDto toClientDetailsActual(ClientInfo model) {
		ClientDetailsDto dto = new ClientDetailsDto();
		dto.setClient_id_(model.getClient_id_());
		dto.setAccessTokenValiditySeconds(model.getAccessTokenValiditySeconds());
		dto.setAuthorizedGrantTypes(model.getAuthorizedGrantTypes());
		dto.setClientSecret(model.getClientSecret());
		dto.setRefreshTokenValiditySeconds(model.getRefreshTokenValiditySeconds());
		dto.setRegisteredRedirectUri(model.getRegisteredRedirectUri());
		dto.setResourceIds(model.getResourceIds());
		dto.setScope(model.getScope());
		return dto;
	}

}
