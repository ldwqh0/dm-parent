package com.dm.security.oauth2.support.dto;

import java.util.Set;
import java.util.UUID;

import com.dm.common.dto.AuditableDto;
import com.dm.common.dto.IdentifiableDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ClientDto extends AuditableDto implements IdentifiableDto<UUID> {

    private static final long serialVersionUID = 2506603023979985011L;

    private UUID id;

    private String name;

    private String secret;
    
    private Set<String> authorizedGrantTypes;

}
