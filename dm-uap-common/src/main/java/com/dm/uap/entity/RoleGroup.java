package com.dm.uap.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.dm.common.entity.AbstractEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "dm_role_group_")
public class RoleGroup extends AbstractEntity {
    private static final long serialVersionUID = 3288697536654269610L;

    @Column(name = "name_", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "description_", length = 2000)
    private String description;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    private Set<Role> roles;

    public RoleGroup() {
        super();
    }

    public RoleGroup(String name) {
        super();
        this.name = name;
    }

}
