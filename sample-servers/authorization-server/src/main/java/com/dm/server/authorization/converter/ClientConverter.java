package com.dm.server.authorization.converter;

import com.dm.collections.CollectionUtils;
import com.dm.common.converter.AuditEntityConverter;
import com.dm.server.authorization.dto.ClientDto;
import com.dm.server.authorization.entity.Client;
import org.springframework.stereotype.Component;
import org.xyyh.authorization.client.BaseClientDetails;
import org.xyyh.authorization.client.ClientDetails;

/**
 *
 * <p>Client 转换器</p>
 *
 * @author ldwqh0@outlook.com
 */
@Component
public class ClientConverter implements AuditEntityConverter<Client, ClientDto> {

    @Override
    public Client copyProperties(Client model, ClientDto dto) {
        model.setName(dto.getName());
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
        BaseClientDetails clientDetails = new BaseClientDetails(
                model.getId(),
                model.getSecret(),
                model.isAutoApprove(),
                model.getScopes(),
                model.getRegisteredRedirectUris(),
                model.getAuthorizedGrantTypes(),
                model.getAccessTokenValiditySeconds(),
                model.getRefreshTokenValiditySeconds(),
                model.isRequirePkce());
        // bcd.setAccessTokenValiditySeconds(model.getAccessTokenValiditySeconds());
        // TODO bcd.setAdditionalInformation(model.getAdditionalInformation());
        if (CollectionUtils.isNotEmpty(model.getAuthorities())) {
            // TODO
            // bcd.setAuthorities(model.getAuthorities().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet()));
        }
        // bcd.setAutoApproveScopes(autoApproveScopes);
        // TODO
        // bcd.setRefreshTokenValiditySeconds(model.getRefreshTokenValiditySeconds());
        // bcd.setResourceIds(resourceIds);
        return clientDetails;
    }

}
