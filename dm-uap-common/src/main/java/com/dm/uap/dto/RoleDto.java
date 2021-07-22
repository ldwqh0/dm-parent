package com.dm.uap.dto;

import com.dm.common.dto.IdentifiableDto;
import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

@Data
public class RoleDto implements Serializable, GrantedAuthority, IdentifiableDto<Long> {

    private static final long serialVersionUID = -2204000372762540931L;
    /**
     * 角色id
     */
    private Long id;

    /**
     * 角色组
     */
    private String group;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色信息，用户安全认证
     *
     * @return authority全称
     * @ignore 这个属性不给前端提供
     */
    @Override
    @JsonGetter
    public String getAuthority() {
        return group + "_" + name;
    }
}
