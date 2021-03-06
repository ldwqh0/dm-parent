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

    @Column(name = "role_id_")
    private long id;

    @Column(name = "group_", length = 100)
    private String group;

    @Column(name = "name_", length = 100)
    private String name;

    @Override
    @Transient
    public String getAuthority() {
        return group + "_" + name;
    }
}
