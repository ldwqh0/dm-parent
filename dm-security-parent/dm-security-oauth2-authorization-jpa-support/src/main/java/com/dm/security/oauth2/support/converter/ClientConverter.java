package com.dm.security.oauth2.support.converter;

import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Component;

import com.dm.collections.CollectionUtils;
import com.dm.common.converter.AuditEntityConverter;
import com.dm.security.oauth2.support.dto.ClientDto;
import com.dm.security.oauth2.support.entity.Client;

@Component
public class ClientConverter implements AuditEntityConverter<Client, ClientDto> {

    @Override
    public Client copyProperties(Client model, ClientDto dto) {
        model.setName(dto.getName());
//        model.setSecret(dto.getSecret());
        model.setAuthorizedGrantTypes(dto.getAuthorizedGrantTypes());
        model.setScopes(dto.getScope());
        model.setAccessTokenValiditySeconds(dto.getAccessTokenValiditySeconds());
        model.setRefreshTokenValiditySeconds(dto.getRefreshTokenValiditySeconds());
        return model;
    }

    @Override
    public ClientDto toDtoWithoutAudit(Client model) {
        // TODO Auto-generated method stub
        return null;
    }

    public ClientDetails toClientDetails(Client model) {
        BaseClientDetails bcd = new BaseClientDetails();
        bcd.setClientId(model.getId());
        bcd.setClientSecret(model.getSecret());
        bcd.setAccessTokenValiditySeconds(model.getAccessTokenValiditySeconds());
        bcd.setRegisteredRedirectUri(model.getRegisteredRedirectUri());
        bcd.setAdditionalInformation(model.getAdditionalInformation());
        if (CollectionUtils.isNotEmpty(model.getAuthorities())) {
            bcd.setAuthorities(
                    model.getAuthorities().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet()));
        }
        bcd.setAuthorizedGrantTypes(model.getAuthorizedGrantTypes());
//        bcd.setAutoApproveScopes(autoApproveScopes);
        bcd.setRefreshTokenValiditySeconds(model.getRefreshTokenValiditySeconds());
//        bcd.setResourceIds(resourceIds);
        bcd.setScope(model.getScopes());
        return bcd;
    }

}
