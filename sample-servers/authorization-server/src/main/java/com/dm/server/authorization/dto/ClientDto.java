package com.dm.server.authorization.dto;

import com.dm.common.dto.AuditableDto;
import com.dm.common.dto.IdentifiableDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

/**
 * <p>ClientDto</p>
 *
 * @author ldwqh0@outlook.com
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ClientDto extends AuditableDto implements IdentifiableDto<String> {

    private static final long serialVersionUID = 2506603023979985011L;

    private String id;

    private String name;

    private String secret;

    private Set<String> authorizedGrantTypes;

    private Set<String> scope;

    private Integer accessTokenValiditySeconds;

    private Integer refreshTokenValiditySeconds;
}
