package com.dm.auth.service.impl;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import com.dm.auth.dto.AccessTokenInfoDto;
import com.dm.auth.service.AccessTokenService;

@Service
public class AccessTokenServiceImpl implements AccessTokenService {

	@Autowired
	private TokenStore tokenStore;

	@Override
	public Page<AccessTokenInfoDto> listTokensByClient(String client, Pageable pageable) {
		Collection<OAuth2AccessToken> tokens = tokenStore.findTokensByClientId(client);
		return new PageImpl<>(toAccessTokenInfoDto(tokens), pageable, tokens.size());
	}

	private List<AccessTokenInfoDto> toAccessTokenInfoDto(Collection<OAuth2AccessToken> tokens) {
		return tokens.stream().map(this::toAccessTokenInfoDto).distinct().collect(Collectors.toList());
	}

	private AccessTokenInfoDto toAccessTokenInfoDto(OAuth2AccessToken token) {
		OAuth2Authentication authentication = tokenStore.readAuthentication(token);
		AccessTokenInfoDto result = new AccessTokenInfoDto();
		result.setExpiration(ZonedDateTime.ofInstant(token.getExpiration().toInstant(), ZoneId.systemDefault()));
		result.setScope(token.getScope());
		result.setTokenType(token.getTokenType());
		if (!Objects.isNull(authentication)) {
			Object principal = authentication.getPrincipal();
			if (!Objects.isNull(principal) && principal instanceof UserDetails) {
				result.setUsername(((UserDetails) principal).getUsername());
			}
		}
		result.setValue(token.getValue());
		return result;
	}

}
