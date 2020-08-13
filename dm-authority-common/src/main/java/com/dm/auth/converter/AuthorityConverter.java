package com.dm.auth.converter;

import java.util.Optional;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dm.auth.dto.AuthorityDto;
import com.dm.auth.dto.MenuAuthorityDto;
import com.dm.auth.dto.ResourceAuthorityDto;
import com.dm.auth.entity.Authority;
import com.dm.collections.Sets;
import com.dm.common.converter.Converter;

@Component
public class AuthorityConverter implements Converter<Authority, AuthorityDto> {

    @Autowired
    private MenuConverter menuConverter;

    @Autowired
    private ResourceOperationConverter resourceOperationConverter;

    protected AuthorityDto toDtoActual(Authority model) {
        return null;
    }

    @Override
    public Authority copyProperties(Authority model, AuthorityDto dto) {
        throw new NotImplementedException("该方法未实现");
    }

    public ResourceAuthorityDto toResourceAuthorityDto(Authority authority) {
        ResourceAuthorityDto dto = new ResourceAuthorityDto();
        dto.setRoleName(authority.getRoleName());
        dto.setResourceAuthorities(
                Sets.transform(authority.getResourceOperations(), resourceOperationConverter::toDto));
        return dto;
    }

    public MenuAuthorityDto toMenuAuthorityDto(Authority menuAuthority) {
        MenuAuthorityDto dto = new MenuAuthorityDto();
        dto.setRoleName(menuAuthority.getRoleName());
        dto.setAuthorityMenus(Sets.transform(menuAuthority.getMenus(), menuConverter::toDto));
        return dto;
    }

    @Override
    public AuthorityDto toDto(Authority model) {
        return Optional.ofNullable(model).map(this::toDtoActual).orElse(null);
    }

}
