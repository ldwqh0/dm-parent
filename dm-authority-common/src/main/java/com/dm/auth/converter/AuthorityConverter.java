package com.dm.auth.converter;

import com.dm.auth.dto.MenuAuthorityDto;
import com.dm.auth.dto.ResourceAuthorityDto;
import com.dm.auth.entity.AuthResource;
import com.dm.auth.entity.Authority;
import com.dm.collections.Maps;
import com.dm.collections.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthorityConverter {

    private final MenuConverter menuConverter;

    public AuthorityConverter(MenuConverter menuConverter) {
        this.menuConverter = menuConverter;
    }

    public ResourceAuthorityDto toResourceAuthorityDto(Authority authority) {
        ResourceAuthorityDto dto = new ResourceAuthorityDto();
        dto.setRoleName(authority.getRoleName());
        dto.setResourceAuthorities(Maps.transformKeys(authority.getResourceOperations(), AuthResource::getId));
        return dto;
    }

    public MenuAuthorityDto toMenuAuthorityDto(Authority menuAuthority) {
        MenuAuthorityDto dto = new MenuAuthorityDto();
        dto.setRoleName(menuAuthority.getRoleName());
        dto.setAuthorityMenus(Sets.transform(menuAuthority.getMenus(), menuConverter::toDto));
        return dto;
    }

}
