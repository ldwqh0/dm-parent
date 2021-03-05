package com.dm.uap.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.io.Serializable;

@Data
@Embeddable
public class Role implements GrantedAuthority, Serializable {
    private static final long serialVersionUID = -3154023961155631437L;

    /**
     * 角色id
     */
    @Column(name = "role_id_")
    private long id;

    /**
     * 角色组
     */
    @Column(name = "group_", length = 100)
    private String group;

    /**
     * 角色名称
     */
    @Column(name = "name_", length = 100)
    private String name;

    /**
     * 角色信息，用户安全认证
     *
     * @return authority全称
     * @ignore 这个属性不给前端提供
     */
    @Override
    @Transient
    public String getAuthority() {
        return group + "_" + name;
    }
}
