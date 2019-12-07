package com.dm.auth.converter;

import java.util.Objects;
import java.util.Optional;

import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.stereotype.Component;

import com.dm.auth.dto.ClientDetailsDto;
import com.dm.auth.dto.ClientInfoDto;
import com.dm.auth.entity.ClientInfo;
import com.dm.common.converter.AbstractConverter;

@Component
public class ClientInfoConverter extends AbstractConverter<ClientInfo, ClientInfoDto> {

	@Override
	protected ClientInfoDto toDtoActual(ClientInfo model) {
		ClientInfoDto info = new ClientInfoDto();
		info.setAccessTokenValiditySeconds(model.getAccessTokenValiditySeconds());
		info.setAuthorizedGrantTypes(model.getAuthorizedGrantTypes());
		info.setClientId(model.getClientId());
		info.setClientSecret(model.getClientSecret());
		info.setRefreshTokenValiditySeconds(model.getRefreshTokenValiditySeconds());
		info.setRegisteredRedirectUri(model.getRegisteredRedirectUri());
		info.setResourceIds(model.getResourceIds());
		info.setScope(model.getScope());
		info.setName(model.getName());
		info.setAutoApprove(model.isAutoApprove());
		return info;
	}

	@Override
	public ClientInfo copyProperties(ClientInfo model, ClientInfoDto dto) {
		model.setAccessTokenValiditySeconds(dto.getAccessTokenValiditySeconds());
		model.setAuthorizedGrantTypes(dto.getAuthorizedGrantTypes());
		model.setRefreshTokenValiditySeconds(dto.getRefreshTokenValiditySeconds());
		model.setRegisteredRedirectUri(dto.getRegisteredRedirectUri());
		model.setResourceIds(dto.getResourceIds());
		model.setScope(dto.getScope());
		model.setAutoApprove(Objects.isNull(dto.getAutoApprove()) ? false : dto.getAutoApprove());
		model.setName(dto.getName());
		return model;
	}

	public Optional<ClientDetailsDto> toClientDetails(ClientInfo model) {
		return Optional.ofNullable(model).map(this::toClientDetailsActual);
	}

	public Optional<ClientDetailsDto> toClientDetails(Optional<ClientInfo> model) {
		return model.map(this::toClientDetailsActual);
	}

	private ClientDetailsDto toClientDetailsActual(ClientInfo model) {
		ClientDetailsDto dto = new ClientDetailsDto();
		dto.setClient_id_(model.getClientId());
		dto.setAccessTokenValiditySeconds(model.getAccessTokenValiditySeconds());
		dto.setAuthorizedGrantTypes(model.getAuthorizedGrantTypes());
		dto.setClientSecret(model.getClientSecret());
		dto.setRefreshTokenValiditySeconds(model.getRefreshTokenValiditySeconds());
		dto.setRegisteredRedirectUri(model.getRegisteredRedirectUri());
		dto.setResourceIds(model.getResourceIds());
		dto.setScope(model.getScope());
		dto.setClientName(model.getName());
		return dto;
	}

}
